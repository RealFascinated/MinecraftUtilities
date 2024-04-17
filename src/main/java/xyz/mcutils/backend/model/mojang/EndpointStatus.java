package xyz.mcutils.backend.model.mojang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import xyz.mcutils.backend.common.CachedResponse;
import xyz.mcutils.backend.model.cache.CachedEndpointStatus;

import java.util.Map;

@AllArgsConstructor
@Getter
public class EndpointStatus {

    /**
     * The list of endpoints and their status.
     */
    private final Map<String, Status> endpoints;

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
