package au.com.metriculous.scanner.conflict;

import au.com.metriculous.scanner.domain.Person;
import au.com.metriculous.scanner.domain.PersonWithCount;
import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.init.Scanner;
import au.com.metriculous.scanner.init.ScannerType;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;

import java.util.List;

public class ConflictScanner implements Scanner, ConflictApiResult {
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

    }

    @Override
    public List<PersonWithCount> mostConflictedPeople() {
        return null;
    }

    @Override
    public List<Pair<Person, Person>> mostConflictedPairs() {
        return null;
    }
}
