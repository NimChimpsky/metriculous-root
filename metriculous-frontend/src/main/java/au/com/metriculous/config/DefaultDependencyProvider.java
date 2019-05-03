package au.com.metriculous.config;

import au.com.metriculous.config.framework.DependencyProvider;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.util.RepositoryUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultDependencyProvider implements DependencyProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<Class<?>, Object> dependencies = new HashMap<>(2);

    public DefaultDependencyProvider() {
        Gson gson = new Gson();
        dependencies.put(Gson.class, gson);
        MetriculousScanner metriculousScanner = MetriculousScanner.create(RepositoryUtil.getTestRepo());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(metriculousScanner);
        dependencies.put(MetriculousScanner.class, metriculousScanner);
    }

    @Override
    public Object get(Class<?> clazz) {
        return dependencies.get(clazz);
    }
}
