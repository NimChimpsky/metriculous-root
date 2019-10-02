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
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void run() {
        try {
            List<Ref> refs = new Git(repository).branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : refs) {
                logger.info("ref {}", ref.getName());
            }
        } catch (GitAPIException e) {
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
