package au.com.metriculous.api;

import au.com.metriculous.config.framework.annotations.Controller;
import au.com.metriculous.config.framework.annotations.Get;
import au.com.metriculous.scanner.MetriculousScanner;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Controller
public class RepoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public RepoController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Get("/repo")
    public String repoInfo(Map<String, String> parameters) {
        String repositoryPath = metriculousScanner.getRepository();
        return gson.toJson(repositoryPath);
    }
}
