package xyz.mcutils.backend.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerUtils {

    /**
     * Gets the address of the server.
     *
     * @return the address of the server
     */
    public static String getAddress(String ip, int port) {
        return ip + (port == 25565 ? "" : ":" + port);
    }
}
