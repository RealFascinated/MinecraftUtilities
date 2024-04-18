package xyz.mcutils.backend.repository.redis;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.mcutils.backend.model.cache.CachedMinecraftServer;

/**
 * A cache repository for {@link CachedMinecraftServer}'s.
 *
 * @author Braydon
 */
public interface MinecraftServerCacheRepository extends CrudRepository<CachedMinecraftServer, String> { }