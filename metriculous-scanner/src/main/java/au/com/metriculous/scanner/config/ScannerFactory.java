package au.com.metriculous.scanner.config;

import java.io.IOException;

public interface ScannerFactory {

    Scanner build(String repositoryPath) throws IOException;
}
