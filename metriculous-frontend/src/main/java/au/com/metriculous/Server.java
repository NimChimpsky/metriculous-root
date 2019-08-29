package au.com.metriculous;


import au.com.metricsoftware.ArgumentParser;
import au.com.metricsoftware.PropertyProvider;
import au.com.metricsoftware.StringUtil;
import au.com.metriculous.config.Context;
import au.com.metriculous.config.DefaultDependencyProvider;
import au.com.metriculous.config.framework.ApplicationContext;
import au.com.metriculous.config.framework.RootServlet;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.nio.file.Paths;
import java.util.Optional;

import static io.undertow.Handlers.resource;
import static io.undertow.servlet.Servlets.*;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(final String[] args) {
        Optional<ApplicationConfiguration> optionalConfig = ConfigurationSerializer.read();

        ArgumentParser argumentParser = new ArgumentParser(args);
        PropertyProvider propertyProvider = new PropertyProvider(argumentParser);
        String repositoryPath = propertyProvider.getRepositoryPath();

        if (StringUtil.isPresent(repositoryPath)) {
            optionalConfig = Optional.of(new ApplicationConfiguration(propertyProvider));
        }

        ApplicationConfiguration applicationConfiguration = optionalConfig.get();
        LOGGER.info("Starting the webserver on port {} ", applicationConfiguration.getPortNumber());
        LOGGER.info("Repository to be scanned {} ", applicationConfiguration.getRepositoryPath());
        LOGGER.info("Application available at http://localhost:{}", applicationConfiguration.getPortNumber());
        LOGGER.info("Refresh browser to see latest data until scan is compelte.");
        if (StringUtil.isEmpty(applicationConfiguration.getRepositoryPath())) {
            LOGGER.info("No repository path found, please specify at command line with, for example,  -repoPath /MyPath/MyDir/");
            LOGGER.info("Alternatively buy a license and set in config file");
            LOGGER.info("Can not start metriculous without a repository to scan");
            LOGGER.info("Contact support for further assistance support@metriculous.network");
        }
        ApplicationContext applicationContext = new Context(new DefaultDependencyProvider(applicationConfiguration));

        DeploymentInfo servletBuilder = deployment()
                .setClassLoader(Server.class.getClassLoader())
                .setContextPath(ApplicationContext.getContext())
                .setDeploymentName("api.war")
                .addServletContextAttribute(ApplicationContext.getContext(), applicationContext)
                .addServlets(
                        servlet("au.com.metriculous.config.framework", RootServlet.class)
                                .addMapping("/*")

                );

        DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        HttpHandler servletHandler = null;
        try {
            servletHandler = manager.start();
        } catch (ServletException e) {
            LOGGER.error("DeploymentManager failed to start, contact support");
            throw new RuntimeException(e);
        }
        Undertow server = Undertow.builder()
                                  .addHttpListener(applicationConfiguration.getPortNumber(), "localhost")
                                  .setHandler(createHandler(servletHandler))
                                  //.setHandler(servletHandler)
                                  .build();
        server.start();


    }

    private static HttpHandler createHandler(HttpHandler servletHandler) {
        return Handlers.path()
                       .addExactPath("/", resource(new PathResourceManager(Paths.get("metriculous-frontend/src/main/resources/Index.html"), 100))
                               .setDirectoryListingEnabled(false)) // resolves index.html
                       .addPrefixPath(ApplicationContext.getPath(), servletHandler)
                       .addPrefixPath("/static", resource(new PathResourceManager(Paths.get("metriculous-frontend/src/main/resources/"))));

    }

}
