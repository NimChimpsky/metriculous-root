package au.com.metriculous.scanner.api;

import au.com.metriculous.scanner.api.blame.BlameApiResult;
import au.com.metriculous.scanner.api.tree.TreeTraversalApiResult;

public interface ApiResult {

    BlameApiResult blameResult();

    TreeTraversalApiResult treeTraversalResult();
}
