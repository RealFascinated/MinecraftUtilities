package xyz.mcutils.backend.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.mcutils.backend.service.metric.Metric;

/**
 * A repository for {@link Metric}s.
 *
 * @author Braydon
 */
public interface MetricsRepository extends CrudRepository<Metric<?>, String> { }