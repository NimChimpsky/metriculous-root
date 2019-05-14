package au.com.metriculous.licensing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Created by stephen.batty on 7/9/2018.
 */
public class License {
    private static final Logger logger = LoggerFactory.getLogger(License.class);
    private static final Long MONTHS = 3L;
    private final String email;
    private final int expiration;
    private final String licenseNumber;

    public License(String email, int expiration, String licenseNumber) {
        this.email = email;
        this.expiration = expiration;
        this.licenseNumber = licenseNumber;
    }

    private String getEmail() {
        return email;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public boolean isNotExpired() {
        if (withinBuildTimeFrame()) {
            return true;
        }
        License fromNumber = Crypt.decodeToLicense(licenseNumber);
        int expirationYYYYMMDD = fromNumber.getExpiration();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();
        LocalDate expiresDate = null;
        try {
            expiresDate = LocalDate.parse(String.valueOf(expirationYYYYMMDD), dtf);
        } catch (DateTimeParseException e) {
            logger.error("Unable to parse date from license ", e);
            return false;
        }
        return now.isBefore(expiresDate);
    }

    private boolean withinBuildTimeFrame() {
        try {
            String fileName = getClass().getCanonicalName().replace('.', '/') + ".class";
            URI file = getClass().getClassLoader().getResource(fileName).toURI();
            Long compileDateLong = new File(file).lastModified();
            Date compileDate = new Date(compileDateLong);
            LocalDate localCompileDate = compileDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate compileDateExpires = localCompileDate.plusMonths(MONTHS);
            return compileDateExpires.isBefore(LocalDate.now());
        } catch (URISyntaxException e) {
            logger.error("Unable to find compile date of code ", e);
            return false;
        }
    }

    public static void main(String args[]) {
        License license = new License(null, 0, null);
        boolean months = license.withinBuildTimeFrame();
        logger.info("expired {}", months);
    }
}
