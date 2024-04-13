package xyz.mcutils.backend.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.mcutils.backend.model.cache.CachedPlayerSkinPart;

/**
 * A cache repository for player skin parts.
 * <p>
 * This will allow us to easily lookup a
 * player skin part by it's id.
 * </p>
 */
public interface PlayerSkinPartCacheRepository extends CrudRepository<CachedPlayerSkinPart, String> { }