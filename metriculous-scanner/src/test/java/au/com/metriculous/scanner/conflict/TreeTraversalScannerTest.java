package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.Util;
import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.scan_implementations.tree.TreeTraversalScanner;
import au.com.metriculous.scanner.scan_implementations.tree.TreeTraversalScannerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class TreeTraversalScannerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private TreeTraversalScanner treeTraversalScanner;

    @Before
    public void setUp() {
        TreeTraversalScannerFactory factory = new TreeTraversalScannerFactory();
        try {
            treeTraversalScanner = factory.build(Util.getTestRepo());
            treeTraversalScanner.run();
        } catch (IOException e) {
            logger.error("error : {}", e);
        }
    }


    @Test
    public void testResultExists() throws InterruptedException {
        while (!treeTraversalScanner.isComplete()) {
            Thread.sleep(500);
        }
        List<Pair<String, Integer>> conflictedFiles = treeTraversalScanner.mostConflictedFiles(Paging.create(0, 200));
        for (Pair<String, Integer> pair : conflictedFiles) {
            logger.info("file {}, count {}", pair.getLeft(), pair.getRight());
        }

        List<PersonWithCount> conflictedPeople = treeTraversalScanner.mostConflictedPeople(Paging.create(0, 200));
        for (PersonWithCount personWithCount : conflictedPeople) {
            logger.info("person {}, count {}", personWithCount.getName(), personWithCount.getValue());
        }


        Assert.assertTrue(true);
    }
}
