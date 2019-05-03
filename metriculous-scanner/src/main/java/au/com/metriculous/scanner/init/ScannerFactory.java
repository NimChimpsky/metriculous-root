package au.com.metriculous.scanner.init;

import java.io.IOException;

public interface ScannerFactory {

    Scanner build(String repositoryPath) throws IOException;
}
