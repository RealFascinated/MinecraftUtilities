package xyz.mcutils.backend.repository.redis;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.mcutils.backend.model.cache.CachedPlayerName;

/**
 * A cache repository for player usernames.
 * <p>
 * This will allow us to easily lookup a
 * player's username and get their uuid.
 * </p>
 *
 * @author Braydon
 */
public interface PlayerNameCacheRepository extends CrudRepository<CachedPlayerName, String> { }