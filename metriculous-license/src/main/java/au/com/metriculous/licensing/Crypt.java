package au.com.metriculous.licensing;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * If you are here, this is easy to break
 * please don't.
 */
public class Crypt {
    private static final String applicationKeyStr = "decompiled my app please don't steal :sadface:";

    // yeah its not secure
    public static String encode(String raw) {
        try {
            byte[] encoded = Base64.getEncoder().encode(raw.getBytes(StandardCharsets.UTF_8));
            byte[] encodedAgain = Base64.getEncoder().encode(encoded);
            String encodedTwice = new String(encodedAgain, "UTF-8");
            String salty = encodedTwice + applicationKeyStr;
            byte[] encodedThrice = Base64.getEncoder().encode(salty.getBytes(StandardCharsets.UTF_8));
            return new String(encodedThrice, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Licensing encoding issue ", e);
        }
    }

    // yeah its not secure
    public static String decode(String encoded) {
        try {
            byte[] decoded1 = Base64.getDecoder().decode(encoded.getBytes(StandardCharsets.UTF_8));
            String decodedStr = new String(decoded1, "UTF-8");
            String saltRemoved = decodedStr.substring(0, decodedStr.length() - applicationKeyStr.length());
            byte[] nearly = Base64.getDecoder().decode(saltRemoved);
            byte[] decoded = Base64.getDecoder().decode(nearly);
            return new String(decoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Licensing decode issue ", e);
        }
    }

    // yeah its not secure
    public static License decodeToLicense(String licenseNumber) {
        String decodedPleaseDoNotStealMySoftware = decode(licenseNumber);
        int expirationDate = Integer.parseInt(decodedPleaseDoNotStealMySoftware.substring(0, 8));
        String email = decodedPleaseDoNotStealMySoftware.substring(8, decodedPleaseDoNotStealMySoftware.length());
        return new License(email, expirationDate, licenseNumber);
    }


}
