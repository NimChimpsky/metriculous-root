package au.com.metriculous.scanner.init;

import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.util.List;

public interface ScanConfigurer {
    void setFileTypes(List<String> fileTypes);

    TreeFilter getTreeFilter();
}
