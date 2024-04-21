package xyz.mcutils.backend.websocket.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.WebSocketSession;
import xyz.mcutils.backend.Main;
import xyz.mcutils.backend.common.Timer;
import xyz.mcutils.backend.service.MetricService;
import xyz.mcutils.backend.service.metric.metrics.TotalRequestsMetric;
import xyz.mcutils.backend.service.metric.metrics.UniquePlayerLookupsMetric;
import xyz.mcutils.backend.service.metric.metrics.UniqueServerLookupsMetric;
import xyz.mcutils.backend.websocket.WebSocket;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "WebSocket/Metrics")
public class MetricsWebSocket extends WebSocket {
    private final long interval = TimeUnit.SECONDS.toMillis(5);
    private final MetricService metricService;

    public MetricsWebSocket(MetricService metricService) {
        super("/websocket/metrics");
        this.metricService = metricService;

        Timer.scheduleRepeating(() -> {
            for (WebSocketSession session : this.getSessions()) {
                sendMetrics(session);
            }
        }, interval, interval);
    }

    @Override
    public void onSessionConnect(WebSocketSession session) {
        sendMetrics(session);
    }

    /**
     * Sends the metrics to the client.
     *
     * @param session the session to send the metrics to
     */
    private void sendMetrics(WebSocketSession session) {
        try {
            this.sendMessage(session, Main.GSON.toJson(Map.of(
                    "totalRequests", metricService.getMetric(TotalRequestsMetric.class).getValue(),
                    "uniqueServerLookups", metricService.getMetric(UniqueServerLookupsMetric.class).getValue(),
                    "uniquePlayerLookups", metricService.getMetric(UniquePlayerLookupsMetric.class).getValue()
            )));
        } catch (Exception e) {
            log.error("An error occurred while sending metrics to the client", e);
        }
    }
}