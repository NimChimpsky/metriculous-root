package au.com.metriculous.config;

public class PropertyProvider {
    private final String repositoryPath;
    private final int port;

    public PropertyProvider(final String repositoryPath, final int port) {
        this.repositoryPath = repositoryPath;
        this.port = port;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public Integer getPort() {
        return port;
    }

}
