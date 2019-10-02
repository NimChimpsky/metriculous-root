package au.com.metriculous.api;

import au.com.metricsoftware.metrix.annotations.Controller;
import au.com.metricsoftware.metrix.annotations.Get;
import au.com.metriculous.scanner.MetriculousScanner;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.result.DefaultPaging;
import au.com.metriculous.scanner.result.Paging;
import com.google.gson.Gson;

import java.time.ZoneId;
import java.time.zone.ZoneRulesProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class TimeController {

    private final Gson gson;
    private final MetriculousScanner metriculousScanner;

    public TimeController(final Gson gson, final MetriculousScanner metriculousScanner) {
        this.gson = gson;
        this.metriculousScanner = metriculousScanner;
    }

    @Get("/time/full")
    public String weeklyComplete(Map<String, String> parameters) {
        String timeZoneIdStr = parameters.get("TimeZone");
        Paging paging = new DefaultPaging(parameters);
        ZoneId timeZoneId = ZoneId.of(timeZoneIdStr);
        List<Pair<String, Long>> result = metriculousScanner.blameResult()
                                                            .time()
                                                            .weeklyLineCount(timeZoneId, paging);
        return gson.toJson(result);
    }

    @Get("/time/zoneids")
    public String availableTimeZones(Map<String, String> parameters) {
        Set<String> ids = ZoneRulesProvider.getAvailableZoneIds();
        return gson.toJson(ids);
    }
}
