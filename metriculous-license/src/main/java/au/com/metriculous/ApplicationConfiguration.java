package au.com.metriculous;

import au.com.metricsoftware.PropertyProvider;
import au.com.metriculous.licensing.License;
import au.com.metriculous.licensing.ManifestReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by stephen.batty on 7/9/2018.
 */
public class ApplicationConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int portNumber;
    private final int numberOfThreads;
    private final License license;
    private final String repositoryPath;
    private static final ManifestReader manifestReader = new ManifestReader();


    public ApplicationConfiguration(int portNumber, int numberOfThreads, License license, String repositoryPath) {
        this.portNumber = portNumber;
        this.license = license;
        this.numberOfThreads = numberOfThreads;
        this.repositoryPath = repositoryPath;
    }

    public ApplicationConfiguration(PropertyProvider propertyProvider) {
        this.portNumber = propertyProvider.getPort();
        this.repositoryPath = propertyProvider.getRepositoryPath();
        this.numberOfThreads = 1;
        this.license = null;

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

    public String getRepositoryPath() {
        return repositoryPath;
    }
}
