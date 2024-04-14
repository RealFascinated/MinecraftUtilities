package xyz.mcutils.backend.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.mcutils.backend.service.metric.Metric;

/**
 * A repository for {@link Metric}s.
 *
 * @author Braydon
 */
public interface MetricsRepository extends CrudRepository<Metric<?>, String> { }