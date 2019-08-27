package au.com.metriculous;

import au.com.metricsoftware.PropertyProvider;
import au.com.metriculous.licensing.License;
import au.com.metriculous.licensing.ManifestReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by stephen.batty on 7/9/2018.
 */
public class ApplicationConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int portNumber;
    private final int numberOfThreads;
    private final License license;
    private List<String> repositoryPaths;
    private static final ManifestReader manifestReader = new ManifestReader();


    public ApplicationConfiguration(int portNumber, int numberOfThreads, License license, List<String> repositoryPaths) {
        this.portNumber = portNumber;
        this.license = license;
        this.numberOfThreads = numberOfThreads;
        this.repositoryPaths = repositoryPaths;
    }

    public ApplicationConfiguration(PropertyProvider propertyProvider) {
        this.portNumber = propertyProvider.getPort();
        this.repositoryPaths = Arrays.asList(propertyProvider.getRepositoryPath());
        this.numberOfThreads = 1;

    }

    public License getLicense() {
        return null;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean isValidLicense() {
        return license.isValid();
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public List<String> getRepositoryPaths() {
        return repositoryPaths;
    }
}
