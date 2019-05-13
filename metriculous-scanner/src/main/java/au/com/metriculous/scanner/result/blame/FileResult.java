package au.com.metriculous.scanner.result.blame;

import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.result.Paging;

import java.util.List;

public interface FileResult {


    List<PersonWithCount> peopleWithMostLines(String filename, Paging paging);

    /**
     * @param filename query file
     * @param paging   result start and and end index
     * @return ordered list of pairs, timestamp and count
     */
    List<Pair<Integer, Long>> timeLineCount(String filename, Paging paging);
}
