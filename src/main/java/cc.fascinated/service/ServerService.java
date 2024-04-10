package cc.fascinated.service;

import cc.fascinated.EnumUtils;
import cc.fascinated.common.DNSUtils;
import cc.fascinated.common.WebRequest;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.model.cache.CachedMinecraftServer;
import cc.fascinated.model.mojang.MojangProfile;
import cc.fascinated.model.mojang.MojangUsernameToUuid;
import cc.fascinated.model.server.MinecraftServer;
import cc.fascinated.repository.MinecraftServerCacheRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Optional;

@Service @Log4j2
public class ServerService {

    private final MinecraftServerCacheRepository serverCacheRepository;

    @Autowired
    public ServerService(MinecraftServerCacheRepository serverCacheRepository) {
        this.serverCacheRepository = serverCacheRepository;
    }

    /**
     * Ping a server to get the server information.
     *
     * @param platformName the name of the platform
     * @param hostname the hostname of the server
     * @param port the port of the server
     * @return the server
     */
    public CachedMinecraftServer getServer(String platformName, String hostname, int port) {
        MinecraftServer.Platform platform = EnumUtils.getEnumConstant(MinecraftServer.Platform.class, platformName.toUpperCase());
        if (platform == null) {
            throw new BadRequestException("Invalid platform: %s".formatted(platformName));
        }
        String key = "%s-%s:%s".formatted(platformName, hostname, port);

        Optional<CachedMinecraftServer> cached = serverCacheRepository.findById(key);
        if (cached.isPresent()) {
            return cached.get();
        }

        InetSocketAddress address = platform == MinecraftServer.Platform.JAVA ? DNSUtils.resolveSRV(hostname) : null;
        if (address != null) {
            port = port != -1 ? port : platform.getDefaultPort(); // If the port is -1, set it to the default port
            hostname = address.getHostName();
        }

        CachedMinecraftServer server = new CachedMinecraftServer(
                key,
                platform.getPinger().ping(hostname, port),
                System.currentTimeMillis()
        );
        serverCacheRepository.save(server);
        server.setCached(-1); // Indicate that the server is not cached
        return server;
    }
}
