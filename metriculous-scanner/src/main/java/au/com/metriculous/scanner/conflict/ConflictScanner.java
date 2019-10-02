package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Triple;
import au.com.metriculous.scanner.init.Scanner;
import au.com.metriculous.scanner.init.ScannerType;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ConflictScanner implements Scanner, ConflictApiResult {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Repository repository;
    private ConflictFileAnalyzer conflictFileAnalyzer;

    public ConflictScanner(Repository repository, ConflictFileAnalyzer fileAnalyzer) { //, ExecutorService executorService) {
//        this.context = context;
        this.repository = repository;
        this.conflictFileAnalyzer = fileAnalyzer;
//        this.executorService = executorService;

    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public String getRepository() {
        return null;
    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.CONFLICT;
    }

    // https://stackoverflow.com/questions/55817243/does-git-keep-a-record-of-past-merge-conflicts
    // https://stackoverflow.com/questions/49500943/what-is-git-rerere-and-how-does-it-work
    @Override
    public void run() {
        try {
            List<Ref> refs = new Git(repository).branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//            for (Ref ref : refs) {
//                logger.info("ref {}", ref.getName());
//            }

            ObjectId head = repository.resolve(Constants.HEAD);
            if (head == null) {
                String message = "Unable to find valid head, check your repo location";
                logger.error(message);
                logger.info("repo {}", repository.getDirectory().toString());
                throw new IOException(message);
            }
            RevWalk revWalk = new RevWalk(repository);
            revWalk.setRevFilter(new RevFilter() {
                @Override
                public boolean include(RevWalk walker, RevCommit cmit) throws StopWalkException, MissingObjectException, IncorrectObjectTypeException, IOException {
                    return cmit.getParentCount() > 1;
                }

                @Override
                public RevFilter clone() {
                    return null;
                }
            });

            RevCommit headCommit = revWalk.parseCommit(head);
            revWalk.markStart(headCommit);
            for (RevCommit revCommit : revWalk) {
                logger.info("shrt message {}", revCommit.getShortMessage());
                logger.info("toString {}", revCommit.toString());
                logger.info("toId {}", revCommit.toObjectId());
            }


        } catch (GitAPIException | IOException e) {
            logger.error("Unable to scan conflicts", e);
        }


    }

    @Override
    public List<PersonWithCount> mostConflictedPeople() {
        return null;
    }

    @Override
    public List<Pair<Person, Person>> mostConflictedPeoplePairs() {
        return null;
    }

    @Override
    public List<Pair<String, Integer>> mostConflictedFiles() {
        return null;
    }

    @Override
    public List<Triple<String, String, Integer>> largestConflicts() {
        return null;
    }
}
