package cc.fascinated.service.pinger;

/**
 * @author Braydon
 * @param <T> the type of server to ping
 */
public interface MinecraftServerPinger<T> {
    T ping(String hostname, int port);
}