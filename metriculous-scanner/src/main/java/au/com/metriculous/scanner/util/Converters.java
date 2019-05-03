package au.com.metriculous.scanner.util;

import java.time.LocalDate;
import java.time.ZoneId;

public class Converters {

    public static Long apply(LocalDate localDate, ZoneId zoneId) {
        return localDate.atStartOfDay(zoneId).toInstant().getEpochSecond();
    }
}
