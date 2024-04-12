package cc.fascinated.repository;

import cc.fascinated.model.cache.CachedPlayer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * A cache repository for {@link CachedPlayer}'s.
 *
 * @author Braydon
 */
public interface PlayerCacheRepository extends CrudRepository<CachedPlayer, UUID> { }