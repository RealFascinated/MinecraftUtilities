package xyz.mcutils.backend.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "xyz.mcutils.backend.repository.mongo")
public class MongoConfig {
}
