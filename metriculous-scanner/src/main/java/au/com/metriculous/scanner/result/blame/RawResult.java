package au.com.metriculous.scanner.result.blame;

import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.StringIntegerTuple;
import au.com.metriculous.scanner.result.Paging;

import java.util.List;

public interface RawResult {
    /**
     * @param paging result start index
     * @return order list of people with count of lines
     */
    List<PersonWithCount> linesInProd(Paging paging);

    List<StringIntegerTuple> filesWithMostAuthors(Paging paging);

    List<Pair<Integer>> timeLineCount(Paging paging);
}
