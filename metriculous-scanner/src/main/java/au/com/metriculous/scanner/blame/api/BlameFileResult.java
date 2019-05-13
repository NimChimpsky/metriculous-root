package au.com.metriculous.scanner.blame.api;

import au.com.metriculous.scanner.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.blame.BlameResultDataStore;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.result.Paging;
import au.com.metriculous.scanner.result.blame.FileResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlameFileResult implements FileResult {
    private final BlameResultDataStore dataStore;

    public BlameFileResult(BlameBasedFileAnalyzer fileAnalyzer) {
        dataStore = fileAnalyzer.getResultDataStore();
    }

    @Override
    public List<PersonWithCount> peopleWithMostLines(String filename, Paging paging) {
        Map<Person, Long> map = dataStore.getAuthorsPerFile().get(filename);
        List<PersonWithCount> tuples = new ArrayList<>(map.size());
        for (Map.Entry<Person, Long> entry : map.entrySet()) {
            PersonWithCount personWithCount = new PersonWithCount(entry.getKey(), entry.getValue());
            tuples.add(personWithCount);
        }

        List<PersonWithCount> resultList = tuples.stream()
                                                 .sorted(Comparator.comparing(PersonWithCount::getValue))
                                                 .collect(Collectors.toList());
        return paging.getSubList(resultList);

    }

    @Override
    public List<Pair<Integer, Long>> timeLineCount(String filename, Paging paging) {
        Map<Integer, AtomicLong> commitTimeCountForFile = dataStore.getLineCountTimeFile().get(filename);
        List<Pair<Integer, Long>> pairs = commitTimeCountForFile.entrySet()
                                                                .stream()
                                                                .map(new Function<Map.Entry<Integer, AtomicLong>, Pair<Integer, Long>>() {
                                                                      @Override
                                                                      public Pair<Integer, Long> apply(Map.Entry<Integer, AtomicLong> entry) {
                                                                          return new Pair<>(entry.getKey(), entry
                                                                                  .getValue()
                                                                                  .longValue());
                                                                      }
                                                                  })
                                                                .collect(Collectors.toList());
        return paging.getSubList(pairs);
    }


}
