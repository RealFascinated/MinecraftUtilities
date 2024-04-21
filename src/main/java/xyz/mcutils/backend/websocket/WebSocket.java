package xyz.mcutils.backend.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor @Getter @Log4j2(topic = "WebSocket")
public abstract class WebSocket extends TextWebSocketHandler {

    /**
     * The sessions that are connected to the WebSocket.
     */
    private final List<WebSocketSession> sessions = new ArrayList<>();

    /**
     * The path of the WebSocket.
     * <p>
     *     Example: /websocket/metrics
     * </p>
     */
    public final String path;

    /**
     * Sends a message to the client.
     *
     * @param session the session to send the message to
     * @param message the message to send
     * @throws IOException if an error occurs while sending the message
     */
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    /**
     * Called when a session connects to the WebSocket.
     *
     * @param session the session that connected
     */
    abstract public void onSessionConnect(WebSocketSession session);

    @Override
    public final void afterConnectionEstablished(@NotNull WebSocketSession session) {
        this.sessions.add(session);
        log.info("Connection established: {}", session.getId());
        this.onSessionConnect(session);
    }

    @Override
    public final void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        this.sessions.remove(session);
        log.info("Connection closed: {}", session.getId());
    }
}
