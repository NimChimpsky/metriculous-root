package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.domain.*;
import au.com.metriculous.scanner.init.DefaultScanConfigurer;
import au.com.metriculous.scanner.init.ScanConfigurer;
import au.com.metriculous.scanner.init.Scanner;
import au.com.metriculous.scanner.init.ScannerType;
import au.com.metriculous.scanner.result.Paging;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;
import org.eclipse.jgit.errors.NoMergeBaseException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.ResolveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ConflictScanner implements Scanner, ConflictApiResult {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Repository repository;
    private ScanConfigurer scanConfigurer = new DefaultScanConfigurer();
    private Map<String, Integer> conflictCountPath = new HashMap<>();
    private Map<Person, Integer> conflictCountPerson = new HashMap<>();
    private final ConflictFileAnalyzer conflictFileAnalyzer;
    private boolean complete = false;

    public ConflictScanner(Repository repository, ConflictFileAnalyzer fileAnalyzer) { //, ExecutorService executorService) {
        this.repository = repository;
        this.conflictFileAnalyzer = fileAnalyzer;
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
        return ScannerType.CONFLICT;
    }

    @Override
    public void setConfig(ScanConfigurer scanConfigurer) {
        this.scanConfigurer = scanConfigurer;
    }

    //https://stackoverflow.com/questions/36372274/how-to-get-conflicts-before-merge-with-jgit
    // https://stackoverflow.com/questions/55817243/does-git-keep-a-record-of-past-merge-conflicts
    // https://stackoverflow.com/questions/49500943/what-is-git-rerere-and-how-does-it-work
    @Override
    public void run() {
        try {
            complete = false;
//            Git git = new Git(repository);
            ObjectId head = repository.resolve(Constants.HEAD);
            if (head == null) {
                String message = "Unable to find valid head, check your repo location";
                logger.error(message);
                logger.info("repo {}", repository.getDirectory().toString());
                throw new IOException(message);
            }
            RevWalk revWalk = new RevWalk(repository);
            revWalk.setRevFilter(new TwoParentFilter());
            RevCommit headCommit = revWalk.parseCommit(head);
            revWalk.markStart(headCommit);
            for (RevCommit revCommit : revWalk) {
                RevCommit[] parents = revCommit.getParents();
                ResolveMerger recursiveMerger = (ResolveMerger) MergeStrategy.RECURSIVE.newMerger(repository, true);
                try {
                    boolean merged = recursiveMerger.merge(parents);
                    if (!merged) {
                        for (RevCommit parent : parents) {
                            logger.info("parent {}", parent.toString());
                            RevWalk parentRevWalk = new RevWalk(repository);
                            RevCommit parentCommit = parentRevWalk.parseCommit(parent);
                            PersonIdent authorIdent = parentCommit.getAuthorIdent();
                            Person person = Person.fromIdent(authorIdent);
                            conflictCountPerson.merge(person, 1, (integer, integer2) -> integer + integer2);
                        }
                        for (String unmergedPath : recursiveMerger.getUnmergedPaths()) {
                            logger.info("unmerged {}", unmergedPath);
                            conflictCountPath.merge(unmergedPath, 1, (integer, integer2) -> integer + integer2);
                        }
                    }
                } catch (NoMergeBaseException e) {
                    logger.error("Unable to merge", e);
                    logger.error("Unable to merge reason", e.getReason());
                }


            }

            complete = true;
        } catch (/*GitAPIException | */IOException e) {
            logger.error("Unable to scan conflicts", e);
        }


    }

    // todo make sublist check size
    @Override
    public List<PersonWithCount> mostConflictedPeople(final Paging paging) {
        Map<Person, Integer> resultCopy = new HashMap<>(conflictCountPerson);
        List<PersonWithCount> personWithCountList = new ArrayList<>(resultCopy.size());
        for (Map.Entry<Person, Integer> entry : resultCopy.entrySet()) {
            personWithCountList.add(new PersonWithCount(entry.getKey(), entry.getValue().longValue()));
        }
        Collections.sort(personWithCountList, PersonWithCount.getCountComparator().reversed());
        return paging.getSubList(personWithCountList);
    }


    @Override
    public List<Pair<Person, Person>> mostConflictedPeoplePairs() {
        return null;
    }

    @Override
    public List<Pair<String, Integer>> mostConflictedFiles(final Paging paging) {
        Map<String, Integer> resultCopy = new HashMap<>(conflictCountPath);
        List<Pair<String, Integer>> conflictedFiles = new ArrayList<>(resultCopy.size());
        for (Map.Entry<String, Integer> entry : resultCopy.entrySet()) {
            conflictedFiles.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        Collections.sort(conflictedFiles, new PairRightComparator().reversed());
        return paging.getSubList(conflictedFiles);
    }

    @Override
    public List<Triple<String, String, Integer>> largestConflicts() {
        return null;
    }
}
