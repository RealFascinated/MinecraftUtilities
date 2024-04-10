package cc.fascinated.model.server;

import cc.fascinated.service.pinger.MinecraftServerPinger;
import cc.fascinated.service.pinger.impl.JavaMinecraftServerPinger;
import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public class MinecraftServer {
    private final String hostname;
    private final String ip;
    private final int port;
    private final String motd;

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
}