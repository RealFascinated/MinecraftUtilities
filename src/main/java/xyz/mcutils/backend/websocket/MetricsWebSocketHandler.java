package xyz.mcutils.backend.websocket;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import xyz.mcutils.backend.Main;
import xyz.mcutils.backend.common.Timer;
import xyz.mcutils.backend.model.metric.WebsocketMetrics;
import xyz.mcutils.backend.service.MetricService;
import xyz.mcutils.backend.service.metric.metrics.TotalPlayerLookupsMetric;
import xyz.mcutils.backend.service.metric.metrics.TotalRequestsMetric;
import xyz.mcutils.backend.service.metric.metrics.TotalServerLookupsMetric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "WebSocket/Metrics")
public class MetricsWebSocketHandler extends TextWebSocketHandler {
    private final long interval = TimeUnit.SECONDS.toMillis(5);
    public static final List<WebSocketSession> SESSIONS = new ArrayList<>();

    private final MetricService metricService;

    public MetricsWebSocketHandler(MetricService metricService) {
        this.metricService = metricService;
        Timer.scheduleRepeating(() -> {
            for (WebSocketSession session : SESSIONS) {
                sendMetrics(session);
            }
        }, interval, interval);
    }

    /**
     * Sends the metrics to the client.
     *
     * @param session the session to send the metrics to
     */
    private void sendMetrics(WebSocketSession session) {
        try {
            WebsocketMetrics metrics = new WebsocketMetrics(Map.of(
                    "totalRequests", metricService.getMetric(TotalRequestsMetric.class).getValue(),
                    "totalServerLookups", metricService.getMetric(TotalServerLookupsMetric.class).getValue(),
                    "totalPlayerLookups", metricService.getMetric(TotalPlayerLookupsMetric.class).getValue()
            ));

            session.sendMessage(new TextMessage(Main.GSON.toJson(metrics)));
        } catch (Exception e) {
            log.error("An error occurred while sending metrics to the client", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connection established with session id: {}", session.getId());

        sendMetrics(session);
        SESSIONS.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
        log.info("WebSocket connection closed with session id: {}", session.getId());

        SESSIONS.remove(session);
    }
}