package au.com.metricsoftware;

public class PropertyProvider {
    private String repositoryPath;
    private Integer port;

    public PropertyProvider(final ArgumentParser argumentParser) {
        this.repositoryPath = argumentParser.getString("-repoPath");
        this.port = argumentParser.getInt("-port");
        if (this.port == null) {
            this.port = 8080;
        }
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public Integer getPort() {
        return port;
    }

}
