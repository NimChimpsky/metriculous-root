package au.com.metriculous.api;

import au.com.metricsoftware.metrix.annotations.Controller;
import au.com.metricsoftware.metrix.annotations.Get;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.api.DefaultPaging;
import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

@Controller
public class MetaStatsController {

    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public MetaStatsController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Get("/meta/totalNumberOfFiles")
    public String totalNumberOfFiles(Map<String, String> parameters) {
        Integer count = metriculousScanner.blameResult().file().totalNumberOfFiles();
        return gson.toJson(count);
    }

    @Get("/meta/totalNumberOfLines")
    public String totalNumberOfLines(Map<String, String> parameters) {
        Long result = metriculousScanner.blameResult().file().totalNumberOfLines();
        return gson.toJson(result);
    }

    @Get("/meta/filesBySize")
    public String largestFiles(Map<String, String> parameters) {
        Paging paging = new DefaultPaging(parameters);
        List<Pair<String, Integer>> result = metriculousScanner.blameResult().file().largestFiles(paging);
        return gson.toJson(result);
    }

    @Get("/meta/fileSize")
    public String fileSize(Map<String, String> parameters) {
        String filePathStr = parameters.get("filePathStr");
        Integer lineCount = metriculousScanner.blameResult().file().sizeOfFile(filePathStr);
        return gson.toJson(lineCount);
    }


    @Get("/file/mostEdits")
    public String mostEdittedFiles(Map<String, String> parameters) {
        Paging paging = new DefaultPaging(parameters);
        List<Pair<String, Integer>> result = metriculousScanner.treeTraversalResult().meta().mostEdittedFiles(paging);
        return gson.toJson(result);
    }
}
