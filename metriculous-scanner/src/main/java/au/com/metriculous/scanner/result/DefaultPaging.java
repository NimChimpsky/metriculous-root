package au.com.metriculous.scanner.result;

import java.util.Map;

public class DefaultPaging implements Paging {
    private final Map<String, String> parameters;

    public DefaultPaging(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Integer start() {
        return Integer.parseInt(parameters.get("start"));
    }

    public Integer end() {
        return Integer.parseInt(parameters.get("end"));
    }

}
