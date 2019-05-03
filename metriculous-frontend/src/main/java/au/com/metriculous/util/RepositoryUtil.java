package au.com.metriculous.util;

/**
 * Created by stephen.batty on 6/4/2018.
 */
public class RepositoryUtil {

    public static String getTestRepo() {
        String os = System.getProperty("os.name").toLowerCase();
        String repoLocation = "/Users/stephenbatty/protean/";
        if (os.indexOf("win") >= 0) {
            repoLocation = "C:\\zRepos\\protean\\";
        }
        return repoLocation;
    }
}
