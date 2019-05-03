package au.com.metriculous.config.framework;

public interface DependencyProvider {

    Object get(Class<?> clazz);
}
