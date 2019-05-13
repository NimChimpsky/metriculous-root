package au.com.metriculous.scanner.result.blame;

import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.Tuple;
import au.com.metriculous.scanner.result.Paging;

import java.util.List;

public interface PersonResult {

    List<StringIntegerTuple> filesWithHighestLineCount(Person person, Paging paging);

    /**
     * @param person query person
     * @param paging result start and and end index
     * @return ordered list of pairs, timestampt and count
     */
    List<Tuple<Integer, Integer>> timeLineCount(Person person, Paging paging);
}
