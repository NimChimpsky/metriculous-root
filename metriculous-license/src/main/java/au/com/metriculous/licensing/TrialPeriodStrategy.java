package au.com.metriculous.licensing;

import java.time.LocalDate;

public class TrialPeriodStrategy {
    //    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private static final Long MONTHS = 1L;
    private static final LocalDate expirationDate = LocalDate.of(2020, 1, 1);
    public boolean isWithinTrialPeriod() {
        return LocalDate.now().isBefore(expirationDate);
    }
//    public boolean isWithinTrialPeriod() {
//        try {
//            String fileName = getClass().getCanonicalName().replace('.', '/') + ".class";
//            URI file = getClass().getClassLoader().getResource(fileName).toURI();
//            Long compileDateLong = new File(file).lastModified();
//            Date compileDate = new Date(compileDateLong);
//            LocalDate localCompileDate = compileDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            LocalDate compileDateExpires = localCompileDate.plusMonths(MONTHS);
//            return compileDateExpires.isBefore(LocalDate.now());
//        } catch (URISyntaxException e) {
//            logger.error("Unable assess trial period, contact support@metriculous.network ", e);
//            return false;
//        }
//    }
}

