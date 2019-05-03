package au.com.metriculous.scanner.blame;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by stephen.batty on 7/10/2018.
 */

public class BlameBasedFileAnalyzer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BlameResultDataStore resultDataStore;

    public BlameBasedFileAnalyzer(BlameResultDataStore resultDataStore) {
        this.resultDataStore = resultDataStore;

    }

    public void analyze(Repository repository, ObjectId commitId, String filePathStr) {
        RevWalk revWalk = new RevWalk(repository);
        BlameCommand blamer = new BlameCommand(repository);
        blamer.setStartCommit(commitId);
        blamer.setFilePath(filePathStr);
        try {
            BlameResult blameResult = blamer.call();
            for (int i = 0; true; i++) {
                try {

                    RevCommit revCommit = blameResult.getSourceCommit(i);
                    revWalk.parseBody(revCommit);
                    resultDataStore.increment(revCommit, filePathStr);
                } catch (ArrayIndexOutOfBoundsException e) {
                    // this is expected, blameresult only returns an item from array
                    // but don't know how long the array is
                    // so need to subclass blameresult and make it better
                    // or just do this
                    break;
                } catch (IOException e) {
                    // logger.debug("breaking {} {}", filePathStr, i);
                    logger.error("problem analyzing file {}", filePathStr, e);
                    break;
                }
            }
        } catch (GitAPIException e) {
            logger.error("problem blaming", e);
        }
    }

    public BlameResultDataStore getResultDataStore() {
        return resultDataStore;
    }
}
