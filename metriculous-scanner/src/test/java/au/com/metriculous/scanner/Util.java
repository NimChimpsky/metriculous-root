package au.com.metriculous.scanner;

public class Util {

    public static String getTestRepo() {
        String os = System.getProperty("os.name").toLowerCase();
        String repoLocation = "/Users/stephenbatty/protean/";
        if (os.indexOf("win") >= 0) {
            repoLocation = "C:\\zRepos\\protean\\";
        }
        return repoLocation;
    }
}
