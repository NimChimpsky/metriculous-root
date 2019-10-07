package au.com.metriculous.scanner.result;

import au.com.metriculous.scanner.result.blame.BlameApiResult;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;
import au.com.metriculous.scanner.result.meta.MetaScanApiResult;

public interface ApiResult {

    BlameApiResult blameResult();

    ConflictApiResult conflictResult();

    MetaScanApiResult metaScanResult();
}
