package au.com.metriculous.scanner.blame.api;

import au.com.metriculous.scanner.blame.BlameBasedFileAnalyzer;
import au.com.metriculous.scanner.blame.BlameResultDataStore;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.StringIntegerTuple;
import au.com.metriculous.scanner.result.Paging;
import au.com.metriculous.scanner.result.blame.PersonResult;

import java.util.Comparator;
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
    public List<StringIntegerTuple> filesWithHighestLineCount(Person person, Paging paging) {
        Map<String, Integer> fileLineCountMap = dataStore.getLineCountByPersonAndFile().get(person);

        List<StringIntegerTuple> resultList = fileLineCountMap.entrySet()
                                                              .stream()
                                                              .map(new Function<Map.Entry<String, Integer>, StringIntegerTuple>() {
                                                                  @Override
                                                                  public StringIntegerTuple apply(Map.Entry<String, Integer> entry) {
                                                                      return new StringIntegerTuple(entry.getKey(), entry
                                                                              .getValue());
                                                                  }
                                                              })
                                                              .sorted(Comparator.comparing(StringIntegerTuple::getValue)
                                                                                .reversed())
                                                              .collect(Collectors.toList());

        return paging.getSubList(resultList);
    }

    // this should be optimized/lazy loaded when scan complee
    @Override
    public List<Pair<Integer>> timeLineCount(Person person, Paging paging) {
        Map<Integer, Integer> map = dataStore.getProdLineCountByTime().get(person);
        List<Pair<Integer>> result = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result.add(new Pair<Integer>(entry.getKey(), entry.getValue()));
        }

        return paging.getSubList(result);
    }

}
