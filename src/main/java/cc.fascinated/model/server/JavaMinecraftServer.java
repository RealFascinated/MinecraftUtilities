package cc.fascinated.model.server;

import cc.fascinated.common.JavaMinecraftVersion;
import cc.fascinated.config.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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

    public JavaMinecraftServer(String hostname, String ip, int port, MOTD motd, @NonNull Version version, Players players, Favicon favicon) {
        super(hostname, ip, port, motd);
        this.version = version;
        this.players = players;
        this.favicon = favicon;
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