package au.com.metricsoftware;

public class StringUtil {

    public static boolean isPresent(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isEmpty(String str) {
        return !isPresent(str);
    }
}
