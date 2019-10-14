package au.com.metriculous.scanner.api.blame;

import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.PersonWithCount;

import java.util.List;

public interface FileResult {
    List<Pair<String, Integer>> largestFiles(Paging paging);

    Integer sizeOfFile(String filePathStr);

    List<PersonWithCount> peopleWithMostLines(String filename, Paging paging);

    /**
     * @param filename query file
     * @param paging   result start and and end index
     * @return ordered list of pairs, timestamp and count
     */
    List<Pair<Integer, Long>> timeLineCount(String filename, Paging paging);
}
