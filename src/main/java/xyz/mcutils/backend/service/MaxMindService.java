package xyz.mcutils.backend.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.mcutils.backend.Main;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Log4j2(topic = "MaxMind Service")
public class MaxMindService {
    /**
     * The MaxMind database.
     */
    private static DatabaseReader database;

    /**
     * The location of the MaxMind database.
     */
    private final String databaseName = "maxmind.mmdb";

    /**
     * The MaxMind license key.
     */
    private final String maxMindLicense;

    public MaxMindService(@Value("${maxmind.license}") String maxMindLicense) {
        this.maxMindLicense = maxMindLicense;
        if (maxMindLicense.isBlank()) {
            log.error("The MaxMind license key is not set, please set it in the configuration and try again");
            System.exit(1);
        }

        File databaseFile = loadDatabase();
        try {
            database = new DatabaseReader.Builder(databaseFile).build();
            log.info("Loaded the MaxMind database from '{}'", databaseFile.getAbsolutePath());
        } catch (Exception ex) {
            log.error("Failed to load the MaxMind database, please check the configuration and try again", ex);
            System.exit(1);
        }
    }

    /**
     * Lookup the GeoIP information for the query.
     *
     * @param query The query to lookup
     * @return The GeoIP information
     */
    public static CityResponse lookup(String query) {
        try {
            return database.city(InetAddress.getByName(query));
        } catch (IOException | GeoIp2Exception e) {
            log.error("Failed to lookup the GeoIP information for '{}'", query, e);
            throw new RuntimeException("Failed to lookup the GeoIP information for '%s'".formatted(query));
        }
    }

    @SneakyThrows
    private File loadDatabase() {
        File database = new File("data", databaseName);
        if (database.exists()) {
            return database;
        }

        // Ensure the parent directories exist
        database.getParentFile().mkdirs();

        String downloadUrl = "https://download.maxmind.com/app/geoip_download?edition_id=GeoLite2-City&license_key=%s&suffix=tar.gz";
        HttpResponse<Path> response = Main.HTTP_CLIENT.send(HttpRequest.newBuilder()
                .uri(URI.create(downloadUrl.formatted(maxMindLicense)))
                .build(), HttpResponse.BodyHandlers.ofFile(Files.createTempFile("maxmind", ".tar.gz")));
        Path downloadedFile = response.body();

        File tempDir = Files.createTempDirectory("maxmind").toFile();

        TarGZipUnArchiver archiver = new TarGZipUnArchiver();
        archiver.setSourceFile(downloadedFile.toFile());
        archiver.setDestDirectory(tempDir);
        archiver.extract();

        File[] files = tempDir.listFiles();
        if (files == null || files.length == 0) {
            log.error("Failed to extract the MaxMind database");
            System.exit(1);
        }

        // Search for the database file
        for (File file : files) {
            // The database is in a subdirectory
            if (!file.isDirectory()) {
                continue;
            }

            // Get the database file
            File databaseFile = new File(file, "GeoLite2-City.mmdb");
            if (!databaseFile.exists()) {
                log.error("Failed to find the MaxMind database in the extracted files");
                continue;
            }
            Files.copy(databaseFile.toPath(), database.toPath());
        }

        log.info("Downloaded and extracted the MaxMind database to '{}'", database.getAbsolutePath());
        return database;
    }
}
