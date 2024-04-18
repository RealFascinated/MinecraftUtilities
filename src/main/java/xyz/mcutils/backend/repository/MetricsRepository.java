package xyz.mcutils.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import xyz.mcutils.backend.service.metric.Metric;

/**
 * A repository for {@link Metric}s.
 *
 * @author Braydon
 */
@EnableMongoRepositories
public interface MetricsRepository extends MongoRepository<Metric<?>, String> { }