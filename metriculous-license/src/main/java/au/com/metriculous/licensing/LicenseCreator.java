package au.com.metriculous.licensing;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by stephen.batty on 7/9/2018.
 */
public class LicenseCreator {
    private static Gson gson = new Gson();

    public static void createTxt(License license) {
        String json = gson.toJson(license);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("metriculous.json");
        } catch (IOException e) {

        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(json);

        printWriter.close();
    }

    public static void main(String args[]) {
        String email = args[0];
        String expirationDate = args[1];
        Integer expiration = Integer.parseInt(expirationDate);
        License license = encodeToLicense(email, expiration);
        createTxt(license);
    }

    public static License encodeToLicense(String email, Integer expirationDate) {
        String licenseNumber = expirationDate + email;
        String encrypted = Crypt.encode(licenseNumber);
        return new License(email, expirationDate, encrypted);
    }
}
