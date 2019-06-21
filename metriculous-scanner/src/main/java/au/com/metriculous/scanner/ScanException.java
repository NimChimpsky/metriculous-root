package au.com.metriculous.scanner;

public class ScanException extends Exception {

    public ScanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScanException(String message) {
        super(message);
    }
}
