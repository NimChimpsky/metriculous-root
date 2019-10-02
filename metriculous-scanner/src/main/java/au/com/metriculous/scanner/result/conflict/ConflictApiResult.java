package au.com.metriculous.scanner.result.conflict;

import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Triple;

import java.util.List;

public interface ConflictApiResult {

    List<PersonWithCount> mostConflictedPeople();

    List<Pair<Person, Person>> mostConflictedPeoplePairs();

    List<Pair<String, Integer>> mostConflictedFiles();

    List<Triple<String, String, Integer>> largestConflicts();
}
