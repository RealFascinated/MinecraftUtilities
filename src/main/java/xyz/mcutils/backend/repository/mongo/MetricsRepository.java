package xyz.mcutils.backend.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.mcutils.backend.service.metric.Metric;

/**
 * A repository for {@link Metric}s.
 *
 * @author Braydon
 */
public interface MetricsRepository extends MongoRepository<Metric<?>, String> { }