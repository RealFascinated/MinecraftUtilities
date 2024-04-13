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
    private final Map<String, Status> endpoints;

    /**
     * The cache information about the request.
     */
    private CacheInformation cache;

    public enum Status {
        /**
         * The service is online and operational.
         */
        ONLINE,

        /**
         * The service is online, but may be experiencing issues.
         * This could be due to high load or other issues.
         */
        DEGRADED,

        /**
         * The service is offline and not operational.
         */
        OFFLINE
    }
}