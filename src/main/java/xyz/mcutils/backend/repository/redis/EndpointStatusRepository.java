package xyz.mcutils.backend.repository.redis;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.mcutils.backend.model.cache.CachedEndpointStatus;

/**
 * A cache repository for {@link CachedEndpointStatus}'s.
 *
 * @author Braydon
 */
public interface EndpointStatusRepository extends CrudRepository<CachedEndpointStatus, String> { }