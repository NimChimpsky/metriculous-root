package au.com.metriculous;


import au.com.metricsoftware.ArgumentParser;
import au.com.metricsoftware.PropertyProvider;
import au.com.metricsoftware.StringUtil;
import au.com.metricsoftware.metrix.MetrixServer;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.ScanException;
import au.com.metriculous.scanner.init.ScannerType;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final String[] controllerPackages
            = {"au.com.metriculous.api"};

    public static void main(final String[] args) {
        Optional<ApplicationConfiguration> optionalConfig = ConfigurationSerializer.read();

        ArgumentParser argumentParser = new ArgumentParser(args);
        PropertyProvider propertyProvider = new PropertyProvider(argumentParser);
        String repositoryPath = propertyProvider.getRepositoryPath();

        if (StringUtil.isPresent(repositoryPath)) {
            optionalConfig = Optional.of(new ApplicationConfiguration(propertyProvider));
        }

        ApplicationConfiguration applicationConfiguration = optionalConfig.get();
        LOGGER.info("Repository to be scanned {} ", applicationConfiguration.getRepositoryPath());
        LOGGER.info("Application available at http://localhost:{}", applicationConfiguration.getPortNumber());
        LOGGER.info("Refresh browser to see latest data until scan is complete.");
        if (StringUtil.isEmpty(applicationConfiguration.getRepositoryPath())) {
            LOGGER.info("No repository path found, please specify at command line with, for example,  -repoPath /MyPath/MyDir/");
            LOGGER.info("Alternatively buy a license and set in config file");
            LOGGER.info("Can not start metriculous without a repository to scan");
            LOGGER.info("Contact support for further assistance support@metriculous.network");
        }

        MetrixServer metrixServer = new MetrixServer.Builder().withPort(applicationConfiguration.getPortNumber())
                                                              .withApiUrlPrefix("/api/v1")
                                                              .withControllerPackages(controllerPackages)
                                                              .withDependencies(createObjectGraph(applicationConfiguration))
                                                              .build();
        try {
            metrixServer.start();
            LOGGER.info("started http://localhost:8181/api/v1/raw/person?start=0&end=20 ");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Unable to start up", e);
        }


    }

    private static Map<Class<?>, Object> createObjectGraph(ApplicationConfiguration applicationConfiguration) {
        List<ScannerType> scannerTypeList = Arrays.asList(ScannerType.values());
        Map<Class<?>, Object> dependencyMap = new HashMap<>(2);
        Gson gson = new Gson();
        dependencyMap.put(Gson.class, gson);
        MetriculousScanner metriculousScanner = null;
        try {
            metriculousScanner = MetriculousScanner.create(applicationConfiguration.getRepositoryPath(), scannerTypeList);
            metriculousScanner.run();
            dependencyMap.put(MetriculousScanner.class, metriculousScanner);
        } catch (ScanException e) {
            LOGGER.error("Exception scanning repository", e);

        }
        return dependencyMap;

    }


}
