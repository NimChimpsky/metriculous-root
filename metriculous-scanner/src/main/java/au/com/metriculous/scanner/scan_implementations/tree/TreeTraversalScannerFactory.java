package au.com.metriculous.scanner.scan_implementations.tree;

import au.com.metriculous.scanner.config.ScannerFactory;
import au.com.metriculous.scanner.scan_implementations.blame.BlameResultDataStore;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TreeTraversalScannerFactory implements ScannerFactory {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public TreeTraversalScanner build(String repositoryPath) throws IOException {

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {

            Repository repository = builder.setGitDir(new File(checkPath(repositoryPath)))
                                           .readEnvironment() // scan environment GIT_* variables
                                           .build();

            BlameResultDataStore resultDataStore = new BlameResultDataStore();

            return new TreeTraversalScanner(repository, new TreeTraversalFileAnalyzer());
        } catch (IOException e) {
            logger.error("Can't access repository {}", repositoryPath, e);
            throw e;
        }
    }

    private String checkPath(String repositoryPath) {
        if (repositoryPath.endsWith(java.io.File.separator + ".git")) {
            return repositoryPath;
        }

        if (repositoryPath.endsWith(java.io.File.separator)) {
            return repositoryPath + ".git";
        }
        return repositoryPath + java.io.File.separator + ".git";

    }
}
