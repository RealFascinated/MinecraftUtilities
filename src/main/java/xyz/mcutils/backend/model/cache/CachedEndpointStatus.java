package xyz.mcutils.backend.model.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import xyz.mcutils.backend.common.CachedResponse;
import xyz.mcutils.backend.model.mojang.EndpointStatus;

import java.io.Serializable;
import java.util.List;

@Setter @Getter @EqualsAndHashCode(callSuper = false)
@RedisHash(value = "mojangEndpointStatus", timeToLive = 60L) // 1 minute (in seconds)
public class CachedEndpointStatus extends CachedResponse implements Serializable {

    /**
     * The id for this endpoint cache.
     */
    @Id @NonNull @JsonIgnore
    private final String id;

    /**
     * The endpoint cache.
     */
    private final List<EndpointStatus> endpoints;

    public CachedEndpointStatus(@NonNull String id, List<EndpointStatus> endpoints) {
        super(Cache.defaultCache());
        this.id = id;
        this.endpoints = endpoints;
    }
}