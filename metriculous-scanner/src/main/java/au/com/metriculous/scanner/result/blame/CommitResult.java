package au.com.metriculous.scanner.result.blame;

import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.result.Paging;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public interface CommitResult {
    List<String> commits(LocalDate startDate, LocalDate endDate, ZoneId zoneId, Paging paging);

    List<Pair<Long, String>> commits(String filename, Person person, Paging paging);


}
