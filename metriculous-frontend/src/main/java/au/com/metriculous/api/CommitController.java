package au.com.metriculous.api;

import au.com.metriculous.config.framework.annotations.Controller;
import au.com.metriculous.config.framework.annotations.Get;
import au.com.metriculous.config.framework.annotations.Post;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.result.DefaultPaging;
import au.com.metriculous.scanner.result.Paging;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Controller
public class CommitController {
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public CommitController(Gson gson, MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Post("/commit/file/person")
    public String commitsByFileAndPerson(Map<String, String> parameters, String json) {
        Person person = gson.fromJson(json, Person.class);
        Paging paging = new DefaultPaging(parameters);
        String filename = parameters.get("filename");
        List<Pair<Long, String>> commitList = metriculousScanner.blameResult()
                                                                .commit()
                                                                .commits(filename, person, paging);
        return gson.toJson(commitList);
    }

    @Get("/commit/dateRange")
    public String commitsByDateRange(Map<String, String> parameters) {
        Paging paging = new DefaultPaging(parameters);
        String startDateStr = parameters.get("startDate");
        LocalDate startDate = LocalDate.parse(startDateStr);
        String endDateStr = parameters.get("endDate");
        LocalDate endDate = LocalDate.parse(endDateStr);
        String zoneIdStr = parameters.get("zoneId");
        ZoneId zoneId = ZoneId.of(zoneIdStr);

        List<String> commitList = metriculousScanner.blameResult()
                                                    .commit()
                                                    .commits(startDate, endDate, zoneId, paging);
        return gson.toJson(commitList);
    }
}
