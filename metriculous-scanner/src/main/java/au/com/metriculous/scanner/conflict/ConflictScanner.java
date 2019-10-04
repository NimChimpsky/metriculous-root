package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.domain.*;
import au.com.metriculous.scanner.init.Scanner;
import au.com.metriculous.scanner.init.ScannerType;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;
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
    private ConflictFileAnalyzer conflictFileAnalyzer;
    private Map<String, Integer> conflictCountPath = new HashMap<>();
    private Map<Person, Integer> conflictCountPerson = new HashMap<>();
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
                boolean merged = recursiveMerger.merge(parents);

                if (!merged) {
                    for (RevCommit parent : parents) {
                        logger.info("parent {}", parent.toString());
                        PersonIdent authorIdent = parent.getAuthorIdent();
                        Person person = Person.fromIdent(authorIdent);
                        conflictCountPerson.merge(person, 1, (integer, integer2) -> integer + integer2);
                    }
                    for (String unmergedPath : recursiveMerger.getUnmergedPaths()) {
                        logger.info("unmerged {}", unmergedPath);
                        conflictCountPath.merge(unmergedPath, 1, (integer, integer2) -> integer + integer2);
                    }
                }


            }

            complete = true;
        } catch (/*GitAPIException | */IOException e) {
            logger.error("Unable to scan conflicts", e);
        }


    }

    @Override
    public List<PersonWithCount> mostConflictedPeople() {
        List<PersonWithCount> personWithCountList = new ArrayList<>(conflictCountPerson.size());
        for (Map.Entry<Person, Integer> entry : conflictCountPerson.entrySet()) {
            personWithCountList.add(new PersonWithCount(entry.getKey(), entry.getValue().longValue()));
        }
        Collections.sort(personWithCountList, PersonWithCount.getCountComparator());
        return personWithCountList;
    }

    @Override
    public List<Pair<Person, Person>> mostConflictedPeoplePairs() {
        return null;
    }

    @Override
    public List<Pair<String, Integer>> mostConflictedFiles() {
        List<Pair<String, Integer>> conflictedFiles = new ArrayList<>(conflictCountPath.size());
        for (Map.Entry<String, Integer> entry : conflictCountPath.entrySet()) {
            conflictedFiles.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        Collections.sort(conflictedFiles, new PairRightComparator());
        return conflictedFiles;
    }

    @Override
    public List<Triple<String, String, Integer>> largestConflicts() {
        return null;
    }
}
