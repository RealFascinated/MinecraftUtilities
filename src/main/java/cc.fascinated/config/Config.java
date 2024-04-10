package cc.fascinated.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class Config {
    public static Config INSTANCE;

    @Value("${public-url}")
    private String webPublicUrl;

    @PostConstruct
    public void onInitialize() {
        INSTANCE = this;
    }
}