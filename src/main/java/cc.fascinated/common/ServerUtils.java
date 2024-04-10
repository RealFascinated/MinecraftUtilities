package cc.fascinated.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerUtils {

    /**
     * Get the hostname and port from a hostname string
     *
     * @param hostname the hostname string
     * @return the hostname and port
     */
    public static Tuple<String, Integer> getHostnameAndPort(String hostname) {
        String[] split = hostname.split(":");
        return new Tuple<>(split[0], split.length == 1 ? -1 : Integer.parseInt(split[1]));
    }

    /**
     * Gets the address of the server.
     *
     * @return the address of the server
     */
    public static String getAddress(String ip, int port) {
        return ip + (port == 25565 ? "" : ":" + port);
    }
}
