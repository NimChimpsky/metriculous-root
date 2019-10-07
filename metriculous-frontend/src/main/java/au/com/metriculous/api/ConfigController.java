package au.com.metriculous.api;

import au.com.metricsoftware.metrix.annotations.Controller;
import au.com.metricsoftware.metrix.annotations.Get;
import au.com.metricsoftware.metrix.annotations.Post;
import au.com.metriculous.scanner.MetriculousScanner;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ConfigController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public ConfigController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Get("/repo")
    public String repoInfo(Map<String, String> parameters) {
        String repositoryPath = metriculousScanner.getRepository();
        return gson.toJson(repositoryPath);
    }

    @Post("/file-types")
    public String setFileTypes(Map<String, String> parameters, String json) {
        String fileTypeCsv = gson.fromJson(json, String.class);
        List<String> fileTypes = Arrays.asList(fileTypeCsv.split(","));
        metriculousScanner.config().setFileTypes(fileTypes);
        return gson.toJson(fileTypes);
    }
}
