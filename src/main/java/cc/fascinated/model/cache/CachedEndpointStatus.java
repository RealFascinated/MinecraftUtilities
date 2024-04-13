package cc.fascinated.model.cache;

import cc.fascinated.common.CacheInformation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@Setter @Getter @ToString
@NoArgsConstructor
@RedisHash(value = "mojangEndpointStatus", timeToLive = 60L) // 1 minute (in seconds)
public class CachedEndpointStatus extends CachedResponse implements Serializable {

    /**
     * The id for this endpoint cache.
     */
    @Id @NonNull @JsonIgnore
    private String id;

    /**
     * The list of endpoints and their status.
     */
    private Map<String, Status> endpoints;

    public CachedEndpointStatus(@NonNull String id, Map<String, Status> endpoints) {
        super(CacheInformation.defaultCache());
        this.id = id;
        this.endpoints = endpoints;
    }

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