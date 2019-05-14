package au.com.metriculous;

import au.com.metriculous.licensing.License;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stephen.batty on 7/9/2018.
 */
public class ApplicationConfiguration {

    private final int portNumber;
    private final int numberOfThreads;
    private final License license;
    private Map<String, ConcurrentHashMap<String, Object>> context;
    private List<String> repositoryPaths;


    public ApplicationConfiguration(int portNumber, int numberOfThreads, License license, List<String> repositoryPaths, Map<String, ConcurrentHashMap<String, Object>> context) {
        this.portNumber = portNumber;
        this.license = license;
        this.context = context;
        this.numberOfThreads = numberOfThreads;
        this.repositoryPaths = repositoryPaths;
    }

    public License getLicense() {
        return null;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean isValidLicense() {
        return license.isNotExpired();
    }

    public ConcurrentHashMap<String, Object> getContext(String repositoryPath) {
        return context.get(repositoryPath);
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public List<String> getRepositoryPaths() {
        return repositoryPaths;
    }
}
