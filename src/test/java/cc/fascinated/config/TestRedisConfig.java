package cc.fascinated.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.NonNull;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * Test configuration for
 * a mock Redis server.
 *
 * @author Braydon
 */
@TestConfiguration
public class TestRedisConfig {
    @NonNull private final RedisServer server;

    public TestRedisConfig() throws IOException {
        server = new RedisServer(); // Construct the mock server
    }

    /**
     * Start up the mock Redis server.
     *
     * @throws IOException if there was an issue starting the server
     */
    @PostConstruct
    public void onInitialize() throws IOException {
        server.start();
    }

    /**
     * Shutdown the running mock Redis server.
     *
     * @throws IOException if there was an issue stopping the server
     */
    @PreDestroy
    public void housekeeping() throws IOException {
        server.stop();
    }
}