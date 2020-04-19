package au.com.metricsoftware;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ArgumentParser {
    private final Map<String, String> arguments = new HashMap<>();

    public ArgumentParser(String args[]) {
        for (int i = 0; i < args.length; i++) {
            arguments.put(args[i], args[++i]);
        }
    }

    public <T> T get(String key, Function<String, T> conversionFunction) {
        String value = arguments.get(key);
        return conversionFunction.apply(value);
    }

    public Integer getInt(String key) {
        String value = arguments.get(key);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public String getString(String key) {
        return arguments.get(key);
    }
}
