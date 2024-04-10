package cc.fascinated.model.server;

import cc.fascinated.Main;
import cc.fascinated.common.JavaMinecraftVersion;
import cc.fascinated.common.ServerUtils;
import cc.fascinated.config.Config;
import cc.fascinated.model.mojang.JavaServerStatusToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.awt.*;

/**
 * @author Braydon
 */
@Setter @Getter
public final class JavaMinecraftServer extends MinecraftServer {

    /**
     * The version of the server.
     */
    @NonNull private final Version version;

    /**
     * The players on the server.
     */
    private Players players;

    /**
     * The favicon of the server.
     */
    private Favicon favicon;

    /**
     * The mojang banned status of the server.
     */
    private boolean mojangBanned;

    public JavaMinecraftServer(String hostname, String ip, int port, MOTD motd, @NonNull Version version, Players players, Favicon favicon) {
        super(hostname, ip, port, motd);
        this.version = version;
        this.players = players;
        this.favicon = favicon;
    }

    /**
     * Create a new Java Minecraft server.
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param token the status token
     * @return the Java Minecraft server
     */
    @NonNull
    public static JavaMinecraftServer create(@NonNull String hostname, String ip, int port, @NonNull JavaServerStatusToken token) {
        String motdString = token.getDescription() instanceof String ? (String) token.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = new TextComponent(ComponentSerializer.parse(Main.GSON.toJson(token.getDescription()))).toLegacyText();
        }
        return new JavaMinecraftServer(
                hostname,
                ip,
                port,
                MinecraftServer.MOTD.create(motdString),
                token.getVersion().detailedCopy(),
                token.getPlayers(),
                JavaMinecraftServer.Favicon.create(token.getFavicon(), ServerUtils.getAddress(hostname, port))
        );
    }

    @AllArgsConstructor @Getter
    public static class Version {
        /**
         * The version name of the server.
         */
        @NonNull
        private final String name;

        /**
         * The server platform.
         */
        private String platform;

        /**
         * The protocol version.
         */
        private final int protocol;

        /**
         * The name of the protocol, null if not found.
         */
        private final String protocolName;

        /**
         * Create a more detailed
         * copy of this object.
         *
         * @return the detailed copy
         */
        @NonNull
        public Version detailedCopy() {
            String platform = null;
            if (name.contains(" ")) { // Parse the server platform
                String[] split = name.split(" ");
                if (split.length == 2) {
                    platform = split[0];
                }
            }
            JavaMinecraftVersion minecraftVersion = JavaMinecraftVersion.byProtocol(protocol);
            return new Version(name, platform, protocol, minecraftVersion == null ? null : minecraftVersion.getName());
        }

    }

    @Getter @AllArgsConstructor
    public static class Players {

        /**
         * The maximum amount of players the server can hold.
         */
        private final int max;

        /**
         * The amount of players currently online.
         */
        private final int online;

        /**
         * The sample of players currently online.
         */
        private final String[] sample;
    }

    @Getter @AllArgsConstructor
    public static class Favicon {

        /**
         * The raw base64 of the favicon.
         */
        private final String base64;

        /**
         * The url to the favicon.
         */
        private String url;

        /**
         * Create a new favicon for a server.
         *
         * @param base64 the base64 of the favicon
         * @param address the address of the server
         * @return the new favicon
         */
        public static Favicon create(String base64, @NonNull String address) {
            if (base64 == null) { // The server doesn't have a favicon
                return null;
            }
            return new Favicon(base64, Config.INSTANCE.getWebPublicUrl() + "/server/icon/%s".formatted(address));
        }
    }
}