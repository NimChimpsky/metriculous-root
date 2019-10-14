package au.com.metriculous.api;

import au.com.metricsoftware.metrix.annotations.Controller;
import au.com.metricsoftware.metrix.annotations.Get;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.api.DefaultPaging;
import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

@Controller
public class ConflictController {
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public ConflictController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Get("/conflict/file")
    public String mostConflictedFiles(Map<String, String> parameters) {
        Paging paging = new DefaultPaging(parameters);
        List<Pair<String, Integer>> commitList = metriculousScanner.treeTraversalResult().conflict().mostConflictedFiles(paging);
        return gson.toJson(commitList);
    }

    @Get("/conflict/people")
    public String mostConflictedPeople(Map<String, String> parameters) {
        Paging paging = new DefaultPaging(parameters);
        List<PersonWithCount> peopleList = metriculousScanner.treeTraversalResult().conflict().mostConflictedPeople(paging);
        return gson.toJson(peopleList);
    }
}
