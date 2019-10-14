package au.com.metriculous.scanner.scan_implementations.blame.result;

import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.api.blame.PersonResult;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PairRightReversingComparator;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.scan_implementations.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.scan_implementations.blame.BlameResultDataStore;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlamePersonResult implements PersonResult {
    private final BlameResultDataStore dataStore;

    public BlamePersonResult(BlameBasedFileAnalyzer fileAnalyzer) {
        dataStore = fileAnalyzer.getResultDataStore();
    }

    // this should be optimized/lazy loaded when scan complee
    @Override
    public List<Pair<String, Integer>> filesWithHighestLineCount(Person person, Paging paging) {
        Map<String, Integer> fileLineCountMap = dataStore.getLineCountByPersonAndFile().get(person);

        List<Pair<String, Integer>> resultList = fileLineCountMap.entrySet()
                                                                 .stream()
                                                                 .map(new Function<Map.Entry<String, Integer>, Pair<String, Integer>>() {
                                                                      @Override
                                                                      public Pair<String, Integer> apply(Map.Entry<String, Integer> entry) {
                                                                          return new Pair<>(entry.getKey(), entry
                                                                                  .getValue());
                                                                      }
                                                                  })
                                                                 .sorted(new PairRightReversingComparator())
                                                                 .collect(Collectors.toList());

        return paging.getSubList(resultList);
    }

    // this should be optimized/lazy loaded when scan complee
    @Override
    public List<Pair<Integer, Integer>> timeLineCount(Person person, Paging paging) {
        Map<Integer, Integer> map = dataStore.getProdLineCountByTime().get(person);
        List<Pair<Integer, Integer>> result = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result.add(new Pair<Integer, Integer>(entry.getKey(), entry.getValue()));
        }

        return paging.getSubList(result);
    }

}
