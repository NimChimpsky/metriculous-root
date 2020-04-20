package au.com.metriculous.scanner.blame;

import au.com.metriculous.scanner.TestUtil;
import au.com.metriculous.scanner.api.blame.FileResult;
import au.com.metriculous.scanner.api.blame.PersonResult;
import au.com.metriculous.scanner.api.blame.RawResult;
import au.com.metriculous.scanner.scan_implementations.blame.BlameBasedScanner;
import au.com.metriculous.scanner.scan_implementations.blame.BlameBasedScannerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BlameBasedScannerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private BlameBasedScanner blameBasedScanner;

    @Before
    public void setUp() {
        BlameBasedScannerFactory factory = new BlameBasedScannerFactory();
        try {
            blameBasedScanner = factory.build(TestUtil.getTestRepo());

            blameBasedScanner.run();
            while (!blameBasedScanner.isComplete()) {
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("error : {}", e);
        }
    }

    @Ignore
    @Test
    public void testResultExists() {
        PersonResult peopleResult = blameBasedScanner.people();
        RawResult rawResult = blameBasedScanner.raw();
        FileResult fileResult = blameBasedScanner.file();

        Assert.assertNotNull(peopleResult);
        Assert.assertNotNull(rawResult);
        Assert.assertNotNull(fileResult);
    }
}