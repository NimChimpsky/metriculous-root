package au.com.metriculous.scanner.blame.api;

import au.com.metriculous.scanner.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.blame.BlameResultDataStore;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.StringIntegerTuple;
import au.com.metriculous.scanner.result.Paging;
import au.com.metriculous.scanner.result.blame.RawResult;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlameRawResult implements RawResult {
    private final BlameResultDataStore dataStore;

    public BlameRawResult(BlameBasedFileAnalyzer fileAnalyzer) {
        dataStore = fileAnalyzer.getResultDataStore();
    }


    @Override
    public List<PersonWithCount> linesInProd(Paging paging) {
        List<PersonWithCount> personWithCountList = new LinkedList<>();
        for (Map.Entry<Person, AtomicLong> entry : dataStore.getProdLineCountByPerson().entrySet()) {
            personWithCountList.add(new PersonWithCount(entry.getKey(), entry.getValue().longValue()));
        }
        return paging.getSubList(personWithCountList);
    }

    @Override
    public List<StringIntegerTuple> filesWithMostAuthors(Paging paging) {
        List<StringIntegerTuple> resultList = dataStore.getNumberOfAuthorsPerFile().entrySet()
                                                       .stream()
                                                       .map(new Function<Map.Entry<String, AtomicInteger>, StringIntegerTuple>() {
                                                           @Override
                                                           public StringIntegerTuple apply(Map.Entry<String, AtomicInteger> entry) {
                                                               String file = entry.getKey();
                                                               Integer numberOfAuthors = entry.getValue()
                                                                                              .intValue();
                                                               StringIntegerTuple tuple = new StringIntegerTuple(file, numberOfAuthors);
                                                               return tuple;
                                                           }
                                                       })
                                                       .sorted(Comparator.comparing(StringIntegerTuple::getValue)
                                                                         .reversed())
                                                       .collect(Collectors.toList());

        return paging.getSubList(resultList);

    }

    @Override
    public List<Pair<Integer>> timeLineCount(Paging paging) {
        List<Pair<Integer>> timesAndCounts = new LinkedList<>();
        for (Map.Entry<Integer, AtomicInteger> entry : dataStore.getLineCountByTimeAll().entrySet()) {
            timesAndCounts.add(new Pair<>(entry.getKey(), entry.getValue().get()));
        }
        return paging.getSubList(timesAndCounts);
    }

}