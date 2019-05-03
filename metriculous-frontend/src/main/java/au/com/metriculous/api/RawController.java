package au.com.metriculous.api;

import au.com.metriculous.config.framework.annotations.Controller;
import au.com.metriculous.config.framework.annotations.Get;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.StringIntegerTuple;
import au.com.metriculous.scanner.result.DefaultPaging;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Controller
public class RawController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public RawController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Get("/raw/person")
    public String personLineCount(Map<String, String> parameters) {
        List<PersonWithCount> personWithCountList = metriculousScanner.blameResult()
                                                                      .raw()
                                                                      .linesInProd(new DefaultPaging(parameters));
        return gson.toJson(personWithCountList);
    }

    @Get("/raw/file")
    public String fileAuthorCount(Map<String, String> parameters) {
        List<StringIntegerTuple> resultList = metriculousScanner.blameResult()
                                                                .raw()
                                                                .filesWithMostAuthors(new DefaultPaging(parameters));
        return gson.toJson(resultList);
    }

    @Get("/raw/time")
    public String timestampLineCount(Map<String, String> parameters) {
        List<Pair<Integer>> resultList = metriculousScanner.blameResult()
                                                           .raw()
                                                           .timeLineCount(new DefaultPaging(parameters));
        return gson.toJson(resultList);
    }


}
