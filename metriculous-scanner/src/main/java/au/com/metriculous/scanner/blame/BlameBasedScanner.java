package au.com.metriculous.scanner.blame;

import au.com.metriculous.scanner.blame.api.*;
import au.com.metriculous.scanner.init.Scanner;
import au.com.metriculous.scanner.init.ScannerType;
import au.com.metriculous.scanner.result.blame.*;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by stephen.batty on 7/10/2018.
 */
public class BlameBasedScanner implements Scanner, BlameApiResult {
    //    private final Map<String, Object> context;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //    private final ExecutorService executorService;
    private final Repository repository;
    private final BlameBasedFileAnalyzer fileAnalyzer;
    private boolean complete = false;

    public BlameBasedScanner(Repository repository, BlameBasedFileAnalyzer fileAnalyzer) { //, ExecutorService executorService) {
//        this.context = context;
        this.repository = repository;
        this.fileAnalyzer = fileAnalyzer;
//        this.executorService = executorService;
    }

    @Override
    public void run() {
        try {
            ObjectId head = repository.resolve(Constants.HEAD);
            if (head == null) {
                String message = "Unable to find valid head, check your repo location";
                logger.error(message);
                logger.info("repo {}", repository.getDirectory().toString());

                throw new IOException(message);
            }
            RevWalk revWalk = new RevWalk(repository);

            RevCommit commit = revWalk.parseCommit(head);
            TreeFilter pathSuffixFilter = PathSuffixFilter.create(".java");
            RevTree revTree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(revTree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(pathSuffixFilter);
            int count = 0;
            while (treeWalk.next()) {
                logger.debug("file {}", treeWalk.getPathString());
                fileAnalyzer.analyze(repository, head, treeWalk.getPathString());
                count++;
                if (count == 110) {
                    break; // for testing
                }
            }
            revWalk.dispose();
            complete = true;
            logger.info("File traverser completed successfully");


        } catch (IOException e) {
            complete = true;
            logger.error("File Traversal exception", e);
        }
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public String getRepository() {
        return repository.toString();
    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.BLAME;
    }

    @Override
    public RawResult raw() {
        return new BlameRawResult(fileAnalyzer);
    }

    @Override
    public PersonResult people() {
        return new BlamePersonResult(fileAnalyzer);
    }

    @Override
    public FileResult file() {
        return new BlameFileResult(fileAnalyzer);
    }

    @Override
    public TimeResult time() {
        return new BlameTimeResult(fileAnalyzer);
    }

    @Override
    public CommitResult commit() {
        return new BlameCommitResult(fileAnalyzer);
    }
}
