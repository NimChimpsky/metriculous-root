package au.com.metriculous.scanner.result.blame;

import au.com.metriculous.scanner.domain.Tuple;
import au.com.metriculous.scanner.result.Paging;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

public interface TimeResult {

    List<Tuple<String, Long>> calendarLineCount(Function<ZonedDateTime, String> keyFunction, ZoneId zoneId, Paging paging);

    List<Tuple<String, Long>> monthlyLineCount(ZoneId zoneId, Paging paging);

    List<Tuple<String, Long>> weeklyLineCount(ZoneId zoneId, Paging paging);

    List<Tuple<String, Long>> dailyLineCount(ZoneId zoneId, Paging paging);
}
