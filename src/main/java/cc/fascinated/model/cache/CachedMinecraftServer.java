package cc.fascinated.model.cache;

import cc.fascinated.model.server.MinecraftServer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author Braydon
 */
@AllArgsConstructor @Setter @Getter @ToString
@RedisHash(value = "server", timeToLive = 60L) // 1 minute (in seconds)
public final class CachedMinecraftServer implements Serializable {
    /**
     * The id of this cached server.
     */
    @Id @NonNull @JsonIgnore
    private final String id;

    /**
     * The cached server.
     */
    @NonNull
    private final MinecraftServer server;

    /**
     * The cache information about the request.
     */
    private CacheInformation cache;
}