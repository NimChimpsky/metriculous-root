package au.com.metriculous;

import au.com.metriculous.licensing.Crypt;
import au.com.metriculous.licensing.License;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by stephenbatty on 09/07/2018.
 */
public class ConfigurationSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationSerializer.class);
    private static final String home = System.getProperty("user.home");
    private static final String fileName = "/metriculous.json";
    private static Gson gson = new Gson();

    public static Optional<ApplicationConfiguration> read() {
        List<String> jsonAsCollection = Collections.emptyList();
        try {
            jsonAsCollection = Files.readAllLines(Paths.get(home + fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Problem reading config and license file {}, exception ", (home + fileName), e);
            return Optional.empty();
        }
        String json = String.join("", jsonAsCollection);
        ApplicationConfiguration applicationConfiguration = gson.fromJson(json, ApplicationConfiguration.class);
        return Optional.of(applicationConfiguration);

    }

    public static void write(ApplicationConfiguration applicationConfiguration) {
        String json = gson.toJson(applicationConfiguration);
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            fileWriter = new FileWriter(home + fileName);
            printWriter = new PrintWriter(fileWriter);
            printWriter.print(json);
            printWriter.close();
        } catch (IOException e) {
            LOGGER.error("Problem writing config and license file {}, exception ", (home + fileName), e);
        } finally {
            printWriter.close();
        }

    }

    public static void main(String args[]) {
        String licenseNumber = 20191231 + "sbatty@gmail.com";
        License license = new License("sbatty@gmail.com", 20191231, Crypt.encode(licenseNumber));
        String repositoryPath = "/Users/stephenbatty/protean/.git";
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(8080, 3, license, repositoryPath);
        write(applicationConfiguration);
    }
}
