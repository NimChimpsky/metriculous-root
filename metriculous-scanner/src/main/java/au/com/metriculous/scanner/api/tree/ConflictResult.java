package au.com.metriculous.scanner.api.tree;

import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Triple;

import java.util.List;

public interface ConflictResult {

    List<PersonWithCount> mostConflictedPeople(Paging paging);

    List<Pair<Person, Person>> mostConflictedPeoplePairs();

    List<Pair<String, Integer>> mostConflictedFiles(Paging paging);

    List<Triple<String, String, Integer>> largestConflicts();
}
