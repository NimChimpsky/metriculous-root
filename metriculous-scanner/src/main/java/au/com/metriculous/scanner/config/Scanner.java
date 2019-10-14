package au.com.metriculous.scanner.config;


public interface Scanner extends Runnable {

    boolean isComplete();

    String getRepository();

    ScannerType getScannerType();

    void setConfig(ScanConfigurer scanConfigurer);
}
