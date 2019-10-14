package au.com.metriculous.scanner.scan_implementations.tree;

import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

public class TwoParentFilter extends RevFilter {
    @Override
    public boolean include(RevWalk walker, RevCommit revCommit) throws StopWalkException {
        return revCommit.getParentCount() > 1;
    }

    @Override
    public RevFilter clone() {
        return new TwoParentFilter();
    }
}
