package cc.fascinated.model.server;

import cc.fascinated.common.ColorUtils;
import cc.fascinated.service.pinger.MinecraftServerPinger;
import cc.fascinated.service.pinger.impl.JavaMinecraftServerPinger;
import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public class MinecraftServer {
    private final String hostname;
    private final String ip;
    private final int port;
    private final MOTD motd;

    /**
     * A platform a Minecraft
     * server can operate on.
     */
    @AllArgsConstructor @Getter
    public enum Platform {
        /**
         * The Java edition of Minecraft.
         */
        JAVA(new JavaMinecraftServerPinger(), 25565);

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
}