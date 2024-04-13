package cc.fascinated.model.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor @Setter @Getter @ToString
@RedisHash(value = "mojangEndpointStatus", timeToLive = 60L) // 1 minute (in seconds)
public final class CachedEndpointStatus implements Serializable {

    /**
     * The id for this endpoint cache.
     */
    @Id @NonNull @JsonIgnore
    private final String id;

    /**
     * The list of endpoints and their status.
     */
    private final Map<String, Boolean> endpoints;

    /**
     * The unix timestamp of when this
     * server was cached, -1 if not cached.
     */
    private long cached;
}