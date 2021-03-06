package au.com.metriculous.scanner.scan_implementations.blame;

import au.com.metriculous.scanner.api.blame.*;
import au.com.metriculous.scanner.config.DefaultScanConfigurer;
import au.com.metriculous.scanner.config.ScanConfigurer;
import au.com.metriculous.scanner.config.Scanner;
import au.com.metriculous.scanner.config.ScannerType;
import au.com.metriculous.scanner.scan_implementations.blame.result.*;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created by stephen.batty on 7/10/2018.
 */
public class BlameBasedScanner implements Scanner, BlameApiResult {
    //    private final Map<String, Object> context;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //    private final ExecutorService executorService;
    private final Repository repository;
    private final BlameBasedFileAnalyzer fileAnalyzer;
    private ScanConfigurer scanConfigurer = new DefaultScanConfigurer();
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

            RevTree revTree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(revTree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(scanConfigurer.getTreeFilter());
            int count = 0;
            Date start = new Date();
            while (treeWalk.next()) {
                logger.debug("file {}", treeWalk.getPathString());
                fileAnalyzer.analyze(repository, head, treeWalk.getPathString());
                count++;
                if (count % 100 == 0) {
                    Date now = new Date();
                    long difference = now.getTime() - start.getTime();
                    logger.info("Analyzed {} objects, duration so far {} seconds", count, (difference / 1000));
                }
            }
            revWalk.dispose();
            complete = true;
            Date now = new Date();
            long difference = now.getTime() - start.getTime();
            logger.info("File traversal completed successfully in {} seconds,  for {} objects", (difference / 1000), count);


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
    public void setConfig(ScanConfigurer scanConfigurer) {
        this.scanConfigurer = scanConfigurer;
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
