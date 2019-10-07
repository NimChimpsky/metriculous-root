package au.com.metriculous.scanner.result.meta;

import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.result.DefaultPaging;
import au.com.metriculous.scanner.result.Paging;

import java.util.List;

public interface MetaScanApiResult {

    List<Pair<String, Integer>> mostEdittedFiles(Person person, DefaultPaging defaultPaging);

    List<Pair<String, Integer>> mostEdittedFiles(Paging defaultPaging);

    List<Pair<String, Integer>> largestFiles(Paging paging);
}
