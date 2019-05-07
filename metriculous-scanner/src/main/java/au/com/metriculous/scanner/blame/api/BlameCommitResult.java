package au.com.metriculous.scanner.blame.api;

import au.com.metriculous.scanner.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.blame.BlameResultDataStore;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.Tuple;
import au.com.metriculous.scanner.result.Paging;
import au.com.metriculous.scanner.result.blame.CommitResult;
import au.com.metriculous.scanner.util.Converters;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class BlameCommitResult implements CommitResult {
    private final BlameResultDataStore dataStore;

    public BlameCommitResult(BlameBasedFileAnalyzer fileAnalyzer) {
        dataStore = fileAnalyzer.getResultDataStore();
    }

    @Override
    public List<String> commits(LocalDate startDate, LocalDate endDate, ZoneId zoneId, Paging paging) {
        SortedSet<Tuple<Integer, String>> fullCommitSet = dataStore.getCommitSet();

        Tuple<Integer, String> startTuple = new Tuple<>(Converters.apply(startDate, zoneId).intValue(), "");
        Tuple<Integer, String> endTuple = new Tuple<>(Converters.apply(endDate, zoneId).intValue(), "");
        Set<Tuple<Integer, String>> subSet = fullCommitSet.subSet(startTuple, endTuple);
        List<String> resultList = subSet.stream().map(Tuple::getRight).collect(Collectors.toList());

        return paging.getSubList(resultList);
    }

    @Override
    public List<Tuple<Long, String>> commits(String filename, Person person, Paging paging) {
        Map<String, Map<Person, Set<Tuple<Long, String>>>> fileCommitMap = dataStore.getCommitStoreByFilePerson();
        Map<Person, Set<Tuple<Long, String>>> commitPersonMap = fileCommitMap.get(filename);
        Set<Tuple<Long, String>> resultList = commitPersonMap.get(person);
        return paging.getSubList(new ArrayList<>(resultList));
    }

}
