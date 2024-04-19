package xyz.mcutils.backend.model.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import xyz.mcutils.backend.common.CachedResponse;
import xyz.mcutils.backend.model.server.MinecraftServer;

import java.io.Serializable;

/**
 * @author Braydon
 */
@Setter @Getter @EqualsAndHashCode(callSuper = false)
@RedisHash(value = "server", timeToLive = 60L) // 1 minute (in seconds)
public class CachedMinecraftServer extends CachedResponse implements Serializable {
    /**
     * The id of this cached server.
     */
    @Id @NonNull @JsonIgnore
    private String id;

    /**
     * The cached server.
     */
    @NonNull @JsonUnwrapped
    private MinecraftServer server;

    public CachedMinecraftServer(@NonNull String id, @NonNull MinecraftServer server) {
        super(CachedResponse.Cache.defaultCache());
        this.id = id;
        this.server = server;
    }
}