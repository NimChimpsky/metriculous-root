package au.com.metriculous.licensing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by stephen.batty on 7/9/2018.
 */
public class License {
    private static final Logger logger = LoggerFactory.getLogger(License.class);

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

    public boolean isValid() {
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
}
