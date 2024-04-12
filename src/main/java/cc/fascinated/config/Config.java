package cc.fascinated.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter @Log4j2
@Configuration
public class Config {
    public static Config INSTANCE;

    @Autowired
    private Environment environment;

    @Value("${public-url}")
    private String webPublicUrl;

    /**
     * Whether the server is in production mode.
     */
    private boolean production = false;

    @PostConstruct
    public void onInitialize() {
        INSTANCE = this;
        String environmentProperty = environment.getProperty("ENVIRONMENT", "development");
        production = environmentProperty.equalsIgnoreCase("production"); // Set the production mode
        log.info("Server is running in {} mode", production ? "production" : "development");
    }
}