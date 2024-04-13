package xyz.mcutils.backend.service.pinger;

import cc.fascinated.model.dns.DNSRecord;
import cc.fascinated.model.server.MinecraftServer;

/**
 * @author Braydon
 * @param <T> the type of server to ping
 */
public interface MinecraftServerPinger<T extends MinecraftServer> {
    T ping(String hostname, String ip, int port, DNSRecord[] records);
}