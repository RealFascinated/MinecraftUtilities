package xyz.mcutils.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.mcutils.backend.model.cache.CachedEndpointStatus;

/**
 * A cache repository for {@link CachedEndpointStatus}'s.
 *
 * @author Braydon
 */
@Repository
public interface EndpointStatusRepository extends CrudRepository<CachedEndpointStatus, String> { }