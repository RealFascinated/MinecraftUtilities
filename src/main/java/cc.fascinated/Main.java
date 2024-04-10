package cc.fascinated;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Log4j2
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Minecraft Utilities API",
        version = "1.0",
        description = "Wrapper for the Minecraft APIs to make them easier to use.",
        contact = @Contact(name = "Liam", email = "liam@fascinated.cc", url = "https://fascinated.cc"),
        license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")
))
public class Main {

    public static final Gson GSON = new Gson();
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

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