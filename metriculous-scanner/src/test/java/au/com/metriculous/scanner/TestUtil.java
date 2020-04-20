package au.com.metriculous.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum TestUtil {
    ;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtil.class);

    public static String getTestRepo() {
        try (InputStream input = new FileInputStream(System.getProperty("user.home") + File.separator + "metriculous-dev.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            return prop.getProperty("test.repo");
        } catch (IOException e) {
            LOGGER.error("Unable read test repo location ", e);
            return "Unable to read test repo location from properties file";
        }
    }
}
