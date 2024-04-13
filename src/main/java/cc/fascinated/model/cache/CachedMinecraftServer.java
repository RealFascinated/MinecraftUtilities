package cc.fascinated.model.cache;

import cc.fascinated.common.CacheInformation;
import cc.fascinated.model.server.MinecraftServer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author Braydon
 */
@Setter @Getter @ToString
@NoArgsConstructor
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
    @NonNull
    private MinecraftServer server;

    public CachedMinecraftServer(@NonNull String id, @NonNull MinecraftServer server) {
        super(CacheInformation.defaultCache());
        this.id = id;
        this.server = server;
    }
}