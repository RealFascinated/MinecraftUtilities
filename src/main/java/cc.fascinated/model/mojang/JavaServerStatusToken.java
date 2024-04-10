package cc.fascinated.model.mojang;

import cc.fascinated.model.server.JavaMinecraftServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class JavaServerStatusToken {

    /**
     * The version of the server.
     */
    private final JavaMinecraftServer.Version version;

    /**
     * The players on the server.
     */
    private final JavaMinecraftServer.Players players;

    /**
     * The motd of the server.
     */
    private final String description;

    /**
     * The favicon of the server.
     */
    private final String favicon;
}
