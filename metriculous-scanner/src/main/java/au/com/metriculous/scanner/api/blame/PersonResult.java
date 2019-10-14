package au.com.metriculous.scanner.api.blame;

import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;

import java.util.List;

public interface PersonResult {

    List<Pair<String, Integer>> filesWithHighestLineCount(Person person, Paging paging);

    /**
     * @param person query person
     * @param paging result start and and end index
     * @return ordered list of pairs, timestampt and count
     */
    List<Pair<Integer, Integer>> timeLineCount(Person person, Paging paging);
}
