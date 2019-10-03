package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConflictScannerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private ConflictScanner conflictScanner;

    @Before
    public void setUp() {
        ConflictScannerFactory factory = new ConflictScannerFactory();
        try {
            conflictScanner = factory.build(Util.getTestRepo());

            conflictScanner.run();
            while (!conflictScanner.isComplete()) {
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("error : {}", e);
        }
    }

    @Ignore
    @Test
    public void testResultExists() {
        //
        Assert.assertTrue(false);
    }
}
