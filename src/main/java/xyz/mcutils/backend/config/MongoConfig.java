package xyz.mcutils.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "xyz.mcutils.backend.repository.mongo")
public class MongoConfig { }
