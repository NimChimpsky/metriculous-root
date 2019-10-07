package au.com.metriculous.scanner.init;

import org.eclipse.jgit.treewalk.filter.OrTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.util.ArrayList;
import java.util.List;

public class DefaultScanConfigurer implements ScanConfigurer {
    private final List<String> fileTypes = new ArrayList<>(1);

    public DefaultScanConfigurer() {
        fileTypes.add(".java");
        fileTypes.add(".csharp");
        fileTypes.add(".js");
    }

    @Override
    public void setFileTypes(List<String> fileTypes) {
        fileTypes.clear();
        fileTypes.addAll(fileTypes);
    }

    @Override
    public TreeFilter getTreeFilter() {
        List<TreeFilter> treeFilterList = new ArrayList<>(fileTypes.size());
        for (String fileSuffix : fileTypes) {
            TreeFilter pathSuffixFilter = PathSuffixFilter.create(fileSuffix);
            treeFilterList.add(pathSuffixFilter);
        }

        return OrTreeFilter.create(treeFilterList);
    }
}
