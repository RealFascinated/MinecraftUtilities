package xyz.mcutils.backend.model.server;

import com.maxmind.geoip2.model.CityResponse;
import io.micrometer.common.lang.NonNull;
import lombok.*;
import xyz.mcutils.backend.common.ColorUtils;
import xyz.mcutils.backend.config.Config;
import xyz.mcutils.backend.model.dns.DNSRecord;
import xyz.mcutils.backend.service.pinger.MinecraftServerPinger;
import xyz.mcutils.backend.service.pinger.impl.BedrockMinecraftServerPinger;
import xyz.mcutils.backend.service.pinger.impl.JavaMinecraftServerPinger;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor
@Getter @Setter @EqualsAndHashCode
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
    private final DNSRecord[] records;

    /**
     * The motd for the server.
     */
    private final MOTD motd;

    /**
     * The players on the server.
     */
    private final Players players;

    /**
     * The location of the server.
     */
    private final GeoLocation location;

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
         * The URL to the server preview image.
         */
        private final String preview;

        /**
         * Create a new MOTD from a raw string.
         *
         * @param raw the raw motd string
         * @return the new motd
         */
        @NonNull
        public static MOTD create(@NonNull String hostname, @NonNull Platform platform, @NonNull String raw) {
            String[] rawLines = raw.split("\n"); // The raw lines
            return new MOTD(
                    rawLines,
                    Arrays.stream(rawLines).map(ColorUtils::stripColor).toArray(String[]::new),
                    Arrays.stream(rawLines).map(ColorUtils::toHTML).toArray(String[]::new),
                    Config.INSTANCE.getWebPublicUrl() + "/server/%s/preview/%s".formatted(
                            platform.name().toLowerCase(),hostname)
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

    /**
     * The location of the server.
     */
    @AllArgsConstructor @Getter
    public static class GeoLocation {
        /**
         * The country of the server.
         */
        private final String country;

        /**
         * The region of the server.
         */
        private final String region;

        /**
         * The city of the server.
         */
        private final String city;

        /**
         * The latitude of the server.
         */
        private final double latitude;

        /**
         * The longitude of the server.
         */
        private final double longitude;

        /**
         * Gets the location of the server from Maxmind.
         *
         * @param response the response from Maxmind
         * @return the location of the server
         */
        public static GeoLocation fromMaxMind(CityResponse response) {
            if (response == null) {
                return null;
            }
            return new GeoLocation(
                    response.getCountry().getName(),
                    response.getMostSpecificSubdivision().getName(),
                    response.getCity().getName(),
                    response.getLocation().getLatitude(),
                    response.getLocation().getLongitude()
            );
        }
    }
}