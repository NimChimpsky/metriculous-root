package au.com.metriculous.scanner.blame;

import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.domain.*;
import au.com.metriculous.scanner.result.Paging;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * These tests are dependent on the local test GIT repo used, so they don't always work
 * Need to set up a specific repo for testing
 */
public class MetriculousScannerTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MetriculousScanner metriculousScanner = MetriculousScanner.create(Util.getTestRepo());
    private static final Paging PAGING = new Paging() {
        @Override
        public Integer start() {
            return 0;
        }

        @Override
        public Integer end() {
            return 20;
        }
    };

    @Before
    public void setUp() throws Exception {
        metriculousScanner.run();

        while (!metriculousScanner.isComplete()) {
            Thread.sleep(5000);
        }
    }

    @Test
    public void linesInProdTest() {
        List<PersonWithCount> result = metriculousScanner.blameResult()
                                                         .raw()
                                                         .linesInProd(PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
//        Assert.assertEquals(10, result.size());
        for (PersonWithCount personWithCount : result) {
            logger.info("found {} {} and val {}", personWithCount.getName(), personWithCount.getEmail(), personWithCount
                    .getValue());
        }
    }

    @Test
    public void filesWithMostAuthorsTest() {
        List<StringIntegerTuple> result = metriculousScanner.blameResult()
                                                            .raw()
                                                            .filesWithMostAuthors(PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
//        Assert.assertEquals(5, result.size());
        for (StringIntegerTuple filesAuthors : result) {
            logger.info("found {} with {}", filesAuthors.getLabel(), filesAuthors.getValue());
        }
    }

    @Test
    public void timestampLineCountTest() {
        List<Pair<Integer>> result = metriculousScanner.blameResult()
                                                       .raw()
                                                       .timeLineCount(PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
//        Assert.assertEquals(5, result.size());
        for (Pair<Integer> timestampCount : result) {
            logger.info("found {} with {}", timestampCount.getLeft(), timestampCount.getRight());
        }
    }

    @Test
    public void filenamePersonLineCountTest() {
        List<PersonWithCount> result = metriculousScanner.blameResult()
                                                         .file()
                                                         .peopleWithMostLines("mediator/src/main/java/mediator/MediatorProperties.java", PAGING);
//                                                         .peopleWithMostLines("database/src/main/java/database/dao/infrastructure/SystemAlertDao.java", PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        for (PersonWithCount personWithCount : result) {
            logger.info("found {} with {}", personWithCount.getName(), personWithCount.getValue());
        }
    }

    @Test
    public void filenameTimestampCountTest() {
        List<Pair<Integer, Long>> result = metriculousScanner.blameResult()
                                                             .file()
                                                             .timeLineCount("mediator/src/main/java/mediator/MediatorProperties.java", PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        for (Pair<Integer, Long> pair : result) {
            logger.info("found {} with {}", pair.getLeft(), pair.getRight());
        }
    }

    @Test
    public void monthlyCompleteTest() {
        List<Pair<String, Long>> result = metriculousScanner.blameResult()
                                                            .time()
                                                            .monthlyLineCount(ZoneId.of("Australia/Sydney"), PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        for (Pair<String, Long> pair : result) {
            logger.info("found {} with {}", pair.getLeft(), pair.getRight());
        }
    }

    @Test
    public void commitTest() {
        LocalDate start = LocalDate.of(2016, 9, 01);
        List<String> result = metriculousScanner.blameResult()
                                                .commit()
                                                .commits(start, LocalDate.now(), ZoneId.of("Australia/Sydney"), PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        for (String hash : result) {
            logger.info("found {}", hash);
        }
    }

    @Test
    public void commitFilePersonTest() {
        String filename = "database/src/test/java/database/dao/order/ParentOrderJournalDaoTest.java";
        List<Pair<Long, String>> result = metriculousScanner.blameResult()
                                                            .commit()
                                                            .commits(filename, new Person("yukunix", "yukunix@hotmail.com"), PAGING);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        for (Pair pair : result) {
            logger.info("found {} {}", pair.getLeft(), pair.getRight());
        }
    }

}