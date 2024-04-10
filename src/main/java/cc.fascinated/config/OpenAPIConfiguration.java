package cc.fascinated.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

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
        info.setVersion("1.0");
        info.setDescription("Wrapper for the Minecraft APIs to make them easier to use.");
        info.setContact(contact);
        info.setLicense(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI().servers(List.of(server)).info(info);
    }
 }
