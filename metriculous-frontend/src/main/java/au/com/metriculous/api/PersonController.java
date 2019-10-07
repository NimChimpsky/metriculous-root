package au.com.metriculous.api;

import au.com.metricsoftware.metrix.annotations.Controller;
import au.com.metricsoftware.metrix.annotations.Post;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.result.DefaultPaging;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

@Controller
public class PersonController {
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public PersonController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Post("/person/time")
    public String personTimestampCount(Map<String, String> parameters, String json) {
        Person person = gson.fromJson(json, Person.class);
        List<Pair<Integer, Integer>> result = metriculousScanner.blameResult()
                                                                .people()
                                                                .timeLineCount(person, new DefaultPaging(parameters));
        return gson.toJson(result);
    }

    @Post("/person/file")
    public String personFileLineCount(Map<String, String> parameters, String json) {
        Person person = gson.fromJson(json, Person.class);
        List<Pair<String, Integer>> result = metriculousScanner.blameResult()
                                                               .people()
                                                               .filesWithHighestLineCount(person, new DefaultPaging(parameters));
        return gson.toJson(result);
    }

    @Post("/person/mostEdittedFiles")
    public String personMostEdittedFile(Map<String, String> parameters, String json) {
        Person person = gson.fromJson(json, Person.class);
        List<Pair<String, Integer>> result = metriculousScanner.metaScanResult()
                                                               .mostEdittedFiles(person, new DefaultPaging(parameters));
        return gson.toJson(result);
    }
}
