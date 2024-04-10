package cc.fascinated.model.mojang;

import cc.fascinated.model.server.JavaMinecraftServer;
import cc.fascinated.model.server.MinecraftServer;
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
    private final MinecraftServer.Players players;

    /**
     * The mods running on the server.
     */
    private final MinecraftServer.Mod[] mods;

    /**
     * The plugins running on the server.
     */
    private final MinecraftServer.Plugin[] plugins;

    /**
     * The motd of the server.
     */
    private final Object description;

    /**
     * The favicon of the server.
     */
    private final String favicon;
}
