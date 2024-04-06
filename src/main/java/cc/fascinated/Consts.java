package cc.fascinated;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class Consts {

    @Getter
    private static String SITE_URL;

    @Value("${site-url}")
    public void setSiteUrl(String name) {
        SITE_URL = name;
    }
}
