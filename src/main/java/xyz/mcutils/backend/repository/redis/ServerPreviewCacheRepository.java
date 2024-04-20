package xyz.mcutils.backend.repository.redis;

import org.springframework.data.repository.CrudRepository;
import xyz.mcutils.backend.model.cache.CachedServerPreview;

/**
 * A cache repository for server previews.
 */
public interface ServerPreviewCacheRepository extends CrudRepository<CachedServerPreview, String> { }