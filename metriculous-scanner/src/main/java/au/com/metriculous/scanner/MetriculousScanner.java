package au.com.metriculous.scanner;

import au.com.metriculous.scanner.blame.BlameBasedScannerFactory;
import au.com.metriculous.scanner.init.Scanner;
import au.com.metriculous.scanner.init.ScannerType;
import au.com.metriculous.scanner.result.ApiResult;
import au.com.metriculous.scanner.result.blame.BlameApiResult;
import au.com.metriculous.scanner.result.conflict.ConflictApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.EnumMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MetriculousScanner implements Scanner, ApiResult {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MetriculousScanner.class);
    private final EnumMap<ScannerType, Scanner> scannerMap = new EnumMap<>(ScannerType.class);
    private final String repositoryPath;

    private MetriculousScanner(final String repositoryPath, final Scanner... scanners) {
        this.repositoryPath = repositoryPath;
        for (Scanner scanner : scanners) {
            scannerMap.put(scanner.getScannerType(), scanner);
        }
    }


    @Override
    public boolean isComplete() {
        for (Scanner scanner : scannerMap.values()) {
            if (!scanner.isComplete()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getRepository() {
        return repositoryPath;
    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.COMPOSITE;
    }


    @Override
    public void run() {
        for (Scanner scanner : scannerMap.values()) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(scanner);
        }

    }

    public static MetriculousScanner create(String repositoryPath) {
        BlameBasedScannerFactory blameBasedScannerFactory = new BlameBasedScannerFactory();
        Scanner scanner = null;
        try {
            scanner = blameBasedScannerFactory.build(repositoryPath);
        } catch (IOException e) {
            LOGGER.error("Can't find GIT repo {}", e);
        }
        MetriculousScanner metriculousScanner = new MetriculousScanner(repositoryPath, scanner);
        return metriculousScanner;
    }

    @Override
    public BlameApiResult blameResult() {
        return (BlameApiResult) scannerMap.get(ScannerType.BLAME);
    }

    @Override
    public ConflictApiResult conflictResult() {
        return (ConflictApiResult) scannerMap.get(ScannerType.CONFLICT);

    }
}
