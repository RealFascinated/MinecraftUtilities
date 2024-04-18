package xyz.mcutils.backend.repository.redis;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.mcutils.backend.model.cache.CachedPlayerSkinPart;

/**
 * A cache repository for player skin parts.
 * <p>
 * This will allow us to easily lookup a
 * player skin part by it's id.
 * </p>
 */
@EnableRedisRepositories
@Repository
public interface PlayerSkinPartCacheRepository extends CrudRepository<CachedPlayerSkinPart, String> { }