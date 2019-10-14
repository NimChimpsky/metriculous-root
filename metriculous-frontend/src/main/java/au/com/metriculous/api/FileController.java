package au.com.metriculous.api;

import au.com.metricsoftware.metrix.annotations.Controller;
import au.com.metricsoftware.metrix.annotations.Post;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.api.DefaultPaging;
import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

@Controller
public class FileController {
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public FileController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Post("/file/time")
    public String timeLineCount(Map<String, String> parameters, String jsonBody) {
        Paging paging = new DefaultPaging(parameters);
        String fileName = gson.fromJson(jsonBody, String.class);
        List<Pair<Integer, Long>> result = metriculousScanner.blameResult().file().timeLineCount(fileName, paging);
        return gson.toJson(result);
    }

    @Post("/file/person")
    public String personLineCount(Map<String, String> parameters, String jsonBody) {
        Paging paging = new DefaultPaging(parameters);
        String fileName = gson.fromJson(jsonBody, String.class);
        List<PersonWithCount> result = metriculousScanner.blameResult().file().peopleWithMostLines(fileName, paging);
        return gson.toJson(result);
    }





}
