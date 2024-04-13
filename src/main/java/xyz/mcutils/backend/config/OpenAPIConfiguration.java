package xyz.mcutils.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    /**
     * The build properties of the
     * app, null if the app is not built.
     */
    private final BuildProperties buildProperties;

    @Autowired
    public OpenAPIConfiguration(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI defineOpenAPI() {
        Server server = new Server();
        server.setUrl(Config.INSTANCE.getWebPublicUrl());

        Contact contact = new Contact();
        contact.setName("Liam");
        contact.setEmail("liam@fascinated.cc");
        contact.setUrl("https://fascinated.cc");

        Info info = new Info();
        info.setTitle("Minecraft Utilities API");
        info.setVersion(buildProperties == null ? "N/A" : buildProperties.getVersion());
        info.setDescription("Wrapper for the Minecraft APIs to make them easier to use.");
        info.setContact(contact);
        info.setLicense(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI().servers(List.of(server)).info(info);
    }
 }
