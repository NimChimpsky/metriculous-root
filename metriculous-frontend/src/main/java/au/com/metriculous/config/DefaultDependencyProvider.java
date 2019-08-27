package au.com.metriculous.config;

import au.com.metriculous.ApplicationConfiguration;
import au.com.metriculous.config.framework.DependencyProvider;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.ScanException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DefaultDependencyProvider implements DependencyProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<Class<?>, Object> dependencies = new HashMap<>(2);

    public DefaultDependencyProvider(final ApplicationConfiguration applicationConfiguration) {
        Gson gson = new Gson();
        dependencies.put(Gson.class, gson);
        MetriculousScanner metriculousScanner = null;
        try {
            metriculousScanner = MetriculousScanner.create(applicationConfiguration.getRepositoryPath());
            metriculousScanner.run();
            dependencies.put(MetriculousScanner.class, metriculousScanner);
        } catch (ScanException e) {
            logger.warn(e.getMessage());
        }

    }

    @Override
    public Object get(Class<?> clazz) {
        return dependencies.get(clazz);
    }
}
