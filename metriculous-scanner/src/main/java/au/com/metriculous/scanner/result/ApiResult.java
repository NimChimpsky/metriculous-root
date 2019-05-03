package au.com.metriculous.scanner.result;

import au.com.metriculous.scanner.result.blame.BlameApiResult;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;

public interface ApiResult {

    BlameApiResult blameResult();

    ConflictApiResult conflictResult();

}
