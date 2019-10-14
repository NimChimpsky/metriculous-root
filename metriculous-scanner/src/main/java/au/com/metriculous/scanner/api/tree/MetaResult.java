package au.com.metriculous.scanner.api.tree;

import au.com.metriculous.scanner.api.DefaultPaging;
import au.com.metriculous.scanner.api.Paging;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;

import java.util.List;

public interface MetaResult {

    List<Pair<String, Integer>> mostEdittedFiles(Person person, DefaultPaging defaultPaging);

    List<Pair<String, Integer>> mostEdittedFiles(Paging defaultPaging);

}
