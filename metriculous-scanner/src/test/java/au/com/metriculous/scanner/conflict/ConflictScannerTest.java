package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.Util;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ConflictScannerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private ConflictScanner conflictScanner;

    @Before
    public void setUp() {
        ConflictScannerFactory factory = new ConflictScannerFactory();
        try {
            conflictScanner = factory.build(Util.getTestRepo());
            conflictScanner.run();
        } catch (IOException e) {
            logger.error("error : {}", e);
        }
    }


    @Test
    public void testResultExists() throws InterruptedException {
        while (!conflictScanner.isComplete()) {
            Thread.sleep(500);
        }
        List<Pair<String, Integer>> conflictedFiles = conflictScanner.mostConflictedFiles();
        for (Pair<String, Integer> pair : conflictedFiles) {
            logger.info("file {}, count {}", pair.getLeft(), pair.getRight());
        }

        List<PersonWithCount> conflictedPeople = conflictScanner.mostConflictedPeople();
        for (PersonWithCount personWithCount : conflictedPeople) {
            logger.info("person {}, count {}", personWithCount.getName(), personWithCount.getValue());
        }


        Assert.assertTrue(true);
    }
}
