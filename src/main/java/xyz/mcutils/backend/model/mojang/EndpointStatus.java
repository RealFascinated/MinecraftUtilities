package xyz.mcutils.backend.model.mojang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@RequiredArgsConstructor
@Getter @Setter
public class EndpointStatus {

    /**
     * The name of the service.
     */
    private final String name;

    /**
     * The hostname of the service.
     */
    private final String hostname;

    /**
     * The status of the service.
     */
    private Status status;

    /**
     * Statuses for the endpoint.
     */
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
