package xyz.mcutils.backend.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test configuration for
 * a mock Redis server.
 *
 * @author Braydon
 */
@TestConfiguration
@DataMongoTest()
@ExtendWith(SpringExtension.class)
public class TestMongoConfig { }