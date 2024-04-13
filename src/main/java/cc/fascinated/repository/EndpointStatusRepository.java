package cc.fascinated.repository;

import cc.fascinated.model.cache.CachedEndpointStatus;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for {@link CachedEndpointStatus}'s.
 *
 * @author Braydon
 */
public interface EndpointStatusRepository extends CrudRepository<CachedEndpointStatus, String> { }