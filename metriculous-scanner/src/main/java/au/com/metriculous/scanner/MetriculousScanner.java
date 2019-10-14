package au.com.metriculous.scanner;

import au.com.metriculous.ApplicationConfiguration;
import au.com.metriculous.ConfigurationSerializer;
import au.com.metriculous.licensing.TrialPeriodStrategy;
import au.com.metriculous.scanner.api.ApiResult;
import au.com.metriculous.scanner.api.blame.BlameApiResult;
import au.com.metriculous.scanner.api.tree.TreeTraversalApiResult;
import au.com.metriculous.scanner.config.*;
import au.com.metriculous.scanner.config.Scanner;
import au.com.metriculous.scanner.scan_implementations.blame.BlameBasedScannerFactory;
import au.com.metriculous.scanner.scan_implementations.tree.TreeTraversalScannerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MetriculousScanner implements Scanner, ApiResult {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MetriculousScanner.class);
    private final EnumMap<ScannerType, Scanner> scannerMap = new EnumMap<>(ScannerType.class);
    private final String repositoryPath;
    private ScanConfigurer scanConfigurer = new DefaultScanConfigurer();

    private MetriculousScanner(final String repositoryPath, List<Scanner> scanners) {
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
    public void setConfig(ScanConfigurer scanConfigurer) {
        this.scanConfigurer = scanConfigurer;
    }


    @Override
    public void run() {

        for (Scanner scanner : scannerMap.values()) {
            scanner.setConfig(scanConfigurer);
            LOGGER.info("Scanning started {}", scanner.getRepository());
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(scanner);
        }

    }

    public static MetriculousScanner create(String repositoryPath, List<ScannerType> scannerTypes) throws ScanException {
        List<Scanner> scannerList = Collections.emptyList();
        scannerList = createScanners(repositoryPath, scannerTypes);


        TrialPeriodStrategy trialPeriodStrategy = new TrialPeriodStrategy();
        if (trialPeriodStrategy.isWithinTrialPeriod()) {
            LOGGER.info("Within Trial Period");
            MetriculousScanner metriculousScanner = new MetriculousScanner(repositoryPath, scannerList);
            return metriculousScanner;
        } else {
            LOGGER.info("Trial Period Expired");
            Optional<ApplicationConfiguration> optional = ConfigurationSerializer.read();
            if (optional.isPresent() && optional.get().isValidLicense()) {
                LOGGER.info("Valid License Found");
                MetriculousScanner metriculousScanner = new MetriculousScanner(repositoryPath, scannerList);
                return metriculousScanner;
            } else {
                LOGGER.error("No valid License contact support@metriculous.network");
                throw new ScanException("Invalid License/Expired please contact support@metriculous.network");
            }
        }

    }

    private static List<Scanner> createScanners(String repositoryPath, List<ScannerType> scannerTypes) {
        List<Scanner> scannerList = new ArrayList<>(scannerTypes.size());
        for (ScannerType scannerType : scannerTypes) {
            try {
                switch (scannerType) {
                    case BLAME: {
                        scannerList.add(new BlameBasedScannerFactory().build(repositoryPath));
                        break;
                    }
                    case TREE: {
                        scannerList.add(new TreeTraversalScannerFactory().build(repositoryPath));
                        break;
                    }

                }
            } catch (IOException e) {
                LOGGER.error("Can't find GIT repo {}", e);
                continue;
            }
        }
        return scannerList;
    }

    @Override
    public BlameApiResult blameResult() {
        return (BlameApiResult) scannerMap.get(ScannerType.BLAME);
    }

    @Override
    public TreeTraversalApiResult treeTraversalResult() {
        return (TreeTraversalApiResult) scannerMap.get(ScannerType.TREE);
    }


}
