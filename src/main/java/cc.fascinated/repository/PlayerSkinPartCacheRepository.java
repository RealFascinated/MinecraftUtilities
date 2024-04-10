package cc.fascinated.repository;

import cc.fascinated.model.cache.CachedPlayerName;
import cc.fascinated.model.cache.CachedPlayerSkinPart;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for player skin parts.
 * <p>
 * This will allow us to easily lookup a
 * player skin part by it's id.
 * </p>
 */
public interface PlayerSkinPartCacheRepository extends CrudRepository<CachedPlayerSkinPart, String> { }