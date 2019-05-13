package au.com.metriculous.scanner.result.conflict;

import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Pair;

import java.util.List;

public interface ConflictApiResult {

    List<PersonWithCount> mostConflictedPeople();

    List<Pair<Person, Person>> mostConflictedPairs();
}
