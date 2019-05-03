package au.com.metriculous.scanner.result.conflict;

import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;

import java.util.List;

public interface ConflictApiResult {

    List<PersonWithCount> mostConflictedPeople();

    List<Pair<Person>> mostConflictedPairs();
}
