package au.com.metriculous.scanner.api.blame;

import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;

import java.util.List;

public interface RawResult {
    /**
     * @param paging result start index
     * @return order list of people with count of lines
     */
    List<PersonWithCount> linesInProd(Paging paging);

    List<Pair<String, Integer>> filesWithMostAuthors(Paging paging);

    List<Pair<Integer, Integer>> timeLineCount(Paging paging);
}
