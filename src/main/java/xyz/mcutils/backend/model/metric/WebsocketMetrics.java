package xyz.mcutils.backend.model.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class WebsocketMetrics {
    /**
     * The metrics to send to the client.
     */
    private final Map<String, Object> metrics;
}
