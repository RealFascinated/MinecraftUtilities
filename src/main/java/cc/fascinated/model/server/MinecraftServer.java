package cc.fascinated.model.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public class MinecraftServer {
    private final String hostname;
    private final int port;
    private final String motd;
}