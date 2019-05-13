package au.com.metriculous.scanner.blame.api;

import au.com.metriculous.scanner.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.blame.BlameResultDataStore;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.Pair;
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
        SortedSet<Pair<Integer, String>> fullCommitSet = dataStore.getCommitSet();

        Pair<Integer, String> startPair = new Pair<>(Converters.apply(startDate, zoneId).intValue(), "");
        Pair<Integer, String> endPair = new Pair<>(Converters.apply(endDate, zoneId).intValue(), "");
        Set<Pair<Integer, String>> subSet = fullCommitSet.subSet(startPair, endPair);
        List<String> resultList = subSet.stream().map(Pair::getRight).collect(Collectors.toList());

        return paging.getSubList(resultList);
    }

    @Override
    public List<Pair<Long, String>> commits(String filename, Person person, Paging paging) {
        Map<String, Map<Person, Set<Pair<Long, String>>>> fileCommitMap = dataStore.getCommitStoreByFilePerson();
        Map<Person, Set<Pair<Long, String>>> commitPersonMap = fileCommitMap.get(filename);
        Set<Pair<Long, String>> resultSet = commitPersonMap.get(person);
        List<Pair<Long, String>> resultList = new ArrayList<>(resultSet);
        Collections.sort(resultList);
        Collections.reverse(resultList);
        return paging.getSubList(resultList);
    }

}
