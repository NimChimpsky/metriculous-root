package au.com.metriculous.scanner.config;

import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.util.List;

public interface ScanConfigurer {
    void setFileTypes(List<String> fileTypes);

    TreeFilter getTreeFilter();
}
