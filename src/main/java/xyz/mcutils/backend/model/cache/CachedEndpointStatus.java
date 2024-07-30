package xyz.mcutils.backend.model.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import xyz.mcutils.backend.common.CachedResponse;
import xyz.mcutils.backend.common.MojangServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final List<Map<String, Object>> endpoints;

    public CachedEndpointStatus(@NonNull String id, Map<MojangServer, MojangServer.Status> mojangServers) {
        super(Cache.defaultCache());
        this.id = id;
        this.endpoints = new ArrayList<>();

        for (Map.Entry<MojangServer, MojangServer.Status> entry : mojangServers.entrySet()) {
            MojangServer server = entry.getKey();

            Map<String, Object> serverStatus = new HashMap<>();
            serverStatus.put("name", server.getName());
            serverStatus.put("endpoint", server.getEndpoint());
            serverStatus.put("status", entry.getValue().name());
            endpoints.add(serverStatus);
        }
    }
}