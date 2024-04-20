package xyz.mcutils.backend.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import xyz.mcutils.backend.service.MetricService;
import xyz.mcutils.backend.websocket.impl.MetricsWebSocket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocket
public class WebSocketManager implements WebSocketConfigurer {
    private static final List<WebSocket> WEB_SOCKETS = new ArrayList<>();

    private final MetricService metricService;

    @Autowired
    public WebSocketManager(MetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        registerWebSocket(registry, new MetricsWebSocket(metricService));
    }

    /**
     * Registers a WebSocket.
     *
     * @param registry the registry to register the WebSocket on
     * @param webSocket the WebSocket to register
     */
    private void registerWebSocket(WebSocketHandlerRegistry registry, WebSocket webSocket) {
        registry.addHandler(webSocket, webSocket.getPath()).setAllowedOrigins("*");
        WEB_SOCKETS.add(webSocket);
    }

    /**
     * Gets the total amount of connections.
     *
     * @return the total amount of connections
     */
    public static int getTotalConnections() {
        return WEB_SOCKETS.stream().mapToInt(webSocket -> webSocket.getSessions().size()).sum();
    }
}
