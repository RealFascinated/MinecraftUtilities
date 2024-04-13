package cc.fascinated.model.server;

import cc.fascinated.common.ColorUtils;
import cc.fascinated.model.dns.DNSRecord;
import cc.fascinated.service.pinger.MinecraftServerPinger;
import cc.fascinated.service.pinger.impl.BedrockMinecraftServerPinger;
import cc.fascinated.service.pinger.impl.JavaMinecraftServerPinger;
import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor
@Getter @Setter
public class MinecraftServer {

    /**
     * The hostname of the server.
     */
    private final String hostname;

    /**
     * The IP address of the server.
     */
    private final String ip;

    /**
     * The port of the server.
     */
    private final int port;

    /**
     * The DNS records for the server.
     */
    private final DNSRecord[] dnsRecords;

    /**
     * The motd for the server.
     */
    private final MOTD motd;

    /**
     * The players on the server.
     */
    private final Players players;

    /**
     * A platform a Minecraft
     * server can operate on.
     */
    @AllArgsConstructor @Getter
    public enum Platform {
        /**
         * The Java edition of Minecraft.
         */
        JAVA(new JavaMinecraftServerPinger(), 25565),

        /**
         * The Bedrock edition of Minecraft.
         */
        BEDROCK(new BedrockMinecraftServerPinger(), 19132);

        /**
         * The server pinger for this platform.
         */
        @NonNull
        private final MinecraftServerPinger<?> pinger;

        /**
         * The default server port for this platform.
         */
        private final int defaultPort;
    }

    @AllArgsConstructor @Getter
    public static class MOTD {

        /**
         * The raw motd lines
         */
        private final String[] raw;

        /**
         * The clean motd lines
         */
        private final String[] clean;

        /**
         * The html motd lines
         */
        private final String[] html;

        /**
         * Create a new MOTD from a raw string.
         *
         * @param raw the raw motd string
         * @return the new motd
         */
        @NonNull
        public static MOTD create(@NonNull String raw) {
            String[] rawLines = raw.split("\n"); // The raw lines
            return new MOTD(
                    rawLines,
                    Arrays.stream(rawLines).map(ColorUtils::stripColor).toArray(String[]::new),
                    Arrays.stream(rawLines).map(ColorUtils::toHTML).toArray(String[]::new)
            );
        }
    }

    /**
     * Player count data for a server.
     */
    @AllArgsConstructor @Getter
    public static class Players {
        /**
         * The online players on this server.
         */
        private final int online;

        /**
         * The maximum allowed players on this server.
         */
        private final int max;

        /**
         * A sample of players on this server, null or empty if no sample.
         */
        private final Sample[] sample;

        /**
         * A sample player.
         */
        @AllArgsConstructor @Getter @ToString
        public static class Sample {
            /**
             * The unique id of this player.
             */
            @NonNull private final UUID id;

            /**
             * The name of this player.
             */
            @NonNull private final String name;
        }
    }
}