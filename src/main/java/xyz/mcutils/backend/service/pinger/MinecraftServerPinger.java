package xyz.mcutils.backend.service.pinger;

import xyz.mcutils.backend.model.dns.DNSRecord;
import xyz.mcutils.backend.model.server.MinecraftServer;

/**
 * @author Braydon
 * @param <T> the type of server to ping
 */
public interface MinecraftServerPinger<T extends MinecraftServer> {
    T ping(String hostname, String ip, int port, DNSRecord[] records);
}