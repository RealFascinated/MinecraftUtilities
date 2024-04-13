package xyz.mcutils.backend.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.mcutils.backend.model.cache.CachedEndpointStatus;

/**
 * A cache repository for {@link CachedEndpointStatus}'s.
 *
 * @author Braydon
 */
public interface EndpointStatusRepository extends CrudRepository<CachedEndpointStatus, String> { }