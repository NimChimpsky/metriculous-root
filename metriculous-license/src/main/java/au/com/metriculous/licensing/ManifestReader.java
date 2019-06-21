package au.com.metriculous.licensing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestReader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ManifestReader() {

    }

    public Date getCompileDate() throws IOException {
        Class clazz = ManifestReader.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            return null;
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());
        Attributes attr = manifest.getMainAttributes();
        String value = attr.getValue("Manifest-Version");
        System.out.println("my attribute " + value);
        String timestamp = attr.getValue("compile-timestamp");
        System.out.println("timestamp " + timestamp);
        logger.info("timestamp {}", timestamp);
        return new Date();

    }
}
