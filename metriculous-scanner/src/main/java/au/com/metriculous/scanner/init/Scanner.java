package au.com.metriculous.scanner.init;


public interface Scanner extends Runnable {

    boolean isComplete();

    String getRepository();

    ScannerType getScannerType();

    void setConfig(ScanConfigurer scanConfigurer);
}
