package xyz.mcutils.backend.repository.redis;

import org.springframework.data.repository.CrudRepository;
import xyz.mcutils.backend.model.cache.CachedPlayer;

import java.util.UUID;

/**
 * A cache repository for {@link CachedPlayer}'s.
 *
 * @author Braydon
 */
public interface PlayerCacheRepository extends CrudRepository<CachedPlayer, UUID> { }