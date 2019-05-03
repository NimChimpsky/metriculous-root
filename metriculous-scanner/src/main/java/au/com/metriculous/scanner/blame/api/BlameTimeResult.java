package au.com.metriculous.scanner.blame.api;

import au.com.metriculous.scanner.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.blame.BlameResultDataStore;
import au.com.metriculous.scanner.domain.Tuple;
import au.com.metriculous.scanner.result.Paging;
import au.com.metriculous.scanner.result.blame.TimeResult;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlameTimeResult implements TimeResult {
    private final BlameResultDataStore dataStore;

    public BlameTimeResult(BlameBasedFileAnalyzer fileAnalyzer) {
        dataStore = fileAnalyzer.getResultDataStore();
    }

    @Override
    public List<Tuple<String, Long>> calendarLineCount(Function<ZonedDateTime, String> keyFunction, ZoneId zoneId, Paging paging) {
        Map<String, Long> monthCount = new HashMap<>(12);
        for (Map.Entry<Integer, AtomicInteger> entry : dataStore.getLineCountByTimeAll().entrySet()) {
            Integer commitTimeStamp = entry.getKey();
            Instant instant = Instant.ofEpochSecond(commitTimeStamp);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
            String key = keyFunction.apply(zonedDateTime);
            monthCount.merge(key, entry.getValue().longValue(), new BiFunction<Long, Long, Long>() {
                @Override
                public Long apply(Long aLong, Long aLong2) {
                    return aLong + aLong2;
                }
            });
        }

        List<Tuple<String, Long>> resultList = monthCount.entrySet()
                                                         .stream()
                                                         .map(new Function<Map.Entry<String, Long>, Tuple<String, Long>>() {
                                                             @Override
                                                             public Tuple<String, Long> apply(Map.Entry<String, Long> entry) {
                                                                 // convert to string month
                                                                 return new Tuple(entry.getKey(), entry.getValue());
                                                             }
                                                         })
                                                         .collect(Collectors.toList());

        return paging.getSubList(resultList);
    }

    @Override
    public List<Tuple<String, Long>> monthlyLineCount(ZoneId zoneId, Paging paging) {
        Function<ZonedDateTime, String> keyFunction =
                zonedDateTime -> zonedDateTime.getYear() + "" + zonedDateTime.getMonthValue();
        return calendarLineCount(keyFunction, zoneId, paging);
    }

    @Override
    public List<Tuple<String, Long>> weeklyLineCount(ZoneId zoneId, Paging paging) {
        Function<ZonedDateTime, String> keyFunction = zonedDateTime -> zonedDateTime.getYear() + "" + zonedDateTime.get(IsoFields.WEEK_BASED_YEAR);
        return calendarLineCount(keyFunction, zoneId, paging);
    }

    @Override
    public List<Tuple<String, Long>> dailyLineCount(ZoneId zoneId, Paging paging) {
        Function<ZonedDateTime, String> keyFunction = zonedDateTime -> zonedDateTime.getYear() + "" + zonedDateTime.getMonthValue() + "" + zonedDateTime
                .getDayOfMonth();
        return calendarLineCount(keyFunction, zoneId, paging);
    }
}
