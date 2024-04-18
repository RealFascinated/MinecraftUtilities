package xyz.mcutils.backend.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.NonNull;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * Test configuration for
 * a mock Redis server.
 *
 * @author Braydon
 */
@TestConfiguration @DataMongoTest
public class TestMongoConfig {

}