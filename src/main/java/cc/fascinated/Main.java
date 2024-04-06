package cc.fascinated;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Helper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@SpringBootApplication @Log4j2
public class Main {

    @Getter
    private static final Gson GSON = new Gson();

    @Getter
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @SneakyThrows
    public static void main(String[] args) {
        File config = new File("application.yml");
        if (!config.exists()) { // Saving the default config if it doesn't exist locally
            Files.copy(Objects.requireNonNull(Main.class.getResourceAsStream("/application.yml")), config.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("Saved the default configuration to '{}', please re-launch the application", // Log the default config being saved
                    config.getAbsolutePath()
            );
            return;
        }
        log.info("Found configuration at '{}'", config.getAbsolutePath()); // Log the found config

        SpringApplication.run(Main.class, args);
    }
}