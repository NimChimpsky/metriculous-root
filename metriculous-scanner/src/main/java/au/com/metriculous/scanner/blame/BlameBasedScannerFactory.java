package au.com.metriculous.scanner.blame;

import au.com.metriculous.scanner.init.ScannerFactory;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class BlameBasedScannerFactory implements ScannerFactory {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public BlameBasedScanner build(String repositoryPath) throws IOException {

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {

            Repository repository = builder.setGitDir(new File(checkPath(repositoryPath)))
                                           .readEnvironment() // scan environment GIT_* variables
                                           .build();

            BlameResultDataStore resultDataStore = new BlameResultDataStore();

            return new BlameBasedScanner(repository, new BlameBasedFileAnalyzer(resultDataStore));
        } catch (IOException e) {
            logger.error("Can't access repository {}", repositoryPath, e);
            throw e;
        }
    }

    private String checkPath(String repositoryPath) {
        if (!repositoryPath.endsWith(".git")) {
//            logger.warn("Git repo path doesn't look right, appending .git");
            return repositoryPath + ".git";
        }
        return repositoryPath;

    }
}
