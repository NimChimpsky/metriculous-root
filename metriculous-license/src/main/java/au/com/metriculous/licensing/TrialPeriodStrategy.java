package au.com.metriculous.licensing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TrialPeriodStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final Long MONTHS = 1L;

    public boolean isWithinTrialPeriod() {
        try {
            String fileName = getClass().getCanonicalName().replace('.', '/') + ".class";
            URI file = getClass().getClassLoader().getResource(fileName).toURI();
            Long compileDateLong = new File(file).lastModified();
            Date compileDate = new Date(compileDateLong);
            LocalDate localCompileDate = compileDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate compileDateExpires = localCompileDate.plusMonths(MONTHS);
            return compileDateExpires.isBefore(LocalDate.now());
        } catch (URISyntaxException e) {
            logger.error("Unable assess trial period, contact support@metriculous.network ", e);
            return false;
        }
    }
}

