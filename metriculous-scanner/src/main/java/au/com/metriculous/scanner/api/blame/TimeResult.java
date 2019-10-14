package au.com.metriculous.scanner.api.blame;

import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

public interface TimeResult {

    List<Pair<String, Long>> calendarLineCount(Function<ZonedDateTime, String> keyFunction, ZoneId zoneId, Paging paging);

    List<Pair<String, Long>> monthlyLineCount(ZoneId zoneId, Paging paging);

    List<Pair<String, Long>> weeklyLineCount(ZoneId zoneId, Paging paging);

    List<Pair<String, Long>> dailyLineCount(ZoneId zoneId, Paging paging);
}
