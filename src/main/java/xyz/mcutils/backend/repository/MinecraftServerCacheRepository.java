package xyz.mcutils.backend.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.mcutils.backend.model.cache.CachedMinecraftServer;

/**
 * A cache repository for {@link CachedMinecraftServer}'s.
 *
 * @author Braydon
 */
public interface MinecraftServerCacheRepository extends CrudRepository<CachedMinecraftServer, String> { }