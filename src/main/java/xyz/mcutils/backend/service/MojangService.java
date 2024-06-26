package xyz.mcutils.backend.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.jodah.expiringmap.ExpirationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mcutils.backend.common.AppConfig;
import xyz.mcutils.backend.common.ExpiringSet;
import xyz.mcutils.backend.common.WebRequest;
import xyz.mcutils.backend.model.cache.CachedEndpointStatus;
import xyz.mcutils.backend.model.mojang.EndpointStatus;
import xyz.mcutils.backend.model.token.MojangProfileToken;
import xyz.mcutils.backend.model.token.MojangUsernameToUuidToken;
import xyz.mcutils.backend.repository.redis.EndpointStatusRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service @Log4j2(topic = "Mojang Service") @Getter
public class MojangService {

    /**
     * The splitter and joiner for dots.
     */
    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    private static final Joiner DOT_JOINER = Joiner.on('.');

    /**
     * The Mojang API endpoints.
     */
    private static final String SESSION_SERVER_ENDPOINT = "https://sessionserver.mojang.com";
    private static final String API_ENDPOINT = "https://api.mojang.com";
    private static final String FETCH_BLOCKED_SERVERS = SESSION_SERVER_ENDPOINT + "/blockedservers";

    /**
     * The interval to fetch the blocked servers from Mojang.
     */
    private static final long FETCH_BLOCKED_SERVERS_INTERVAL = TimeUnit.HOURS.toMillis(1L);

    /**
     * Information about the Mojang API endpoints.
     */
    private static final String MOJANG_ENDPOINT_STATUS_KEY = "mojang";
    private static final List<EndpointStatus> MOJANG_ENDPOINTS = List.of(
            new EndpointStatus("Minecraft Textures", "textures.minecraft.net"),
            new EndpointStatus("Minecraft Libraries", "libraries.minecraft.net"),
            new EndpointStatus("Minecraft Services", "api.minecraftservices.com"),
            new EndpointStatus("Mojang Assets", "assets.mojang.com"),
            new EndpointStatus("Mojang API", "api.mojang.com"),
            new EndpointStatus("Mojang Session Server", "sessionserver.mojang.com"));

    @Autowired
    private EndpointStatusRepository mojangEndpointStatusRepository;

    /**
     * A list of banned server hashes provided by Mojang.
     * <p>
     * This is periodically fetched from Mojang, see
     * {@link #fetchBlockedServers()} for more info.
     * </p>
     *
     * @see <a href="https://wiki.vg/Mojang_API#Blocked_Servers">Mojang API</a>
     */
    private List<String> bannedServerHashes;

    /**
     * A cache of blocked server hostnames.
     *
     * @see #isServerHostnameBlocked(String) for more
     */
    private final ExpiringSet<String> blockedServersCache = new ExpiringSet<>(ExpirationPolicy.CREATED, 10L, TimeUnit.MINUTES);

    public MojangService() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchBlockedServers();
            }
        }, 0L, FETCH_BLOCKED_SERVERS_INTERVAL);
    }

    /**
     * Fetch a list of blocked servers from Mojang.
     */
    @SneakyThrows
    private void fetchBlockedServers() {
        log.info("Fetching blocked servers from Mojang");
        try (
                InputStream inputStream = new URL(FETCH_BLOCKED_SERVERS).openStream();
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\n")
        ) {
            List<String> hashes = new ArrayList<>();
            while (scanner.hasNext()) {
                hashes.add(scanner.next());
            }
            bannedServerHashes = Collections.synchronizedList(hashes);
            log.info("Fetched {} banned server hashes", bannedServerHashes.size());
        }
    }

    /**
     * Check if the server with the
     * given hostname is blocked by Mojang.
     *
     * @param hostname the server hostname to check
     * @return whether the hostname is blocked
     */
    public boolean isServerBlocked(@NonNull String hostname) {
        // Remove trailing dots
        while (hostname.charAt(hostname.length() - 1) == '.') {
            hostname = hostname.substring(0, hostname.length() - 1);
        }
        // Is the hostname banned?
        if (isServerHostnameBlocked(hostname)) {
            return true;
        }
        List<String> splitDots = Lists.newArrayList(DOT_SPLITTER.split(hostname)); // Split the hostname by dots
        boolean isIp = splitDots.size() == 4; // Is it an IP address?
        if (isIp) {
            for (String element : splitDots) {
                try {
                    int part = Integer.parseInt(element);
                    if (part >= 0 && part <= 255) { // Ensure the part is within the valid range
                        continue;
                    }
                } catch (NumberFormatException ignored) {
                    // Safely ignore, not a number
                }
                isIp = false;
                break;
            }
        }
        // Check if the hostname is blocked
        if (!isIp && isServerHostnameBlocked("*." + hostname)) {
            return true;
        }
        // Additional checks for the hostname
        while (splitDots.size() > 1) {
            splitDots.remove(isIp ? splitDots.size() - 1 : 0);
            String starredPart = isIp ? DOT_JOINER.join(splitDots) + ".*" : "*." + DOT_JOINER.join(splitDots);
            if (isServerHostnameBlocked(starredPart)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the hash for the given
     * hostname is in the blocked server list.
     *
     * @param hostname the hostname to check
     * @return whether the hostname is blocked
     */
    private boolean isServerHostnameBlocked(@NonNull String hostname) {
        // Check the cache first for the hostname
        if (blockedServersCache.contains(hostname)) {
            return true;
        }
        String hashed = Hashing.sha1().hashBytes(hostname.toLowerCase().getBytes(StandardCharsets.ISO_8859_1)).toString();
        boolean blocked = bannedServerHashes.contains(hashed); // Is the hostname blocked?
        if (blocked) { // Cache the blocked hostname
            blockedServersCache.add(hostname);
        }
        return blocked;
    }

    /**
     * Gets the status of the Mojang APIs.
     *
     * @return the status
     */
    public CachedEndpointStatus getMojangApiStatus() {
        log.info("Getting Mojang API status");
        Optional<CachedEndpointStatus> endpointStatus = mojangEndpointStatusRepository.findById(MOJANG_ENDPOINT_STATUS_KEY);
        if (endpointStatus.isPresent() && AppConfig.isProduction()) {
            log.info("Got cached Mojang API status");
            return endpointStatus.get();
        }

        MOJANG_ENDPOINTS.parallelStream().forEach(endpoint -> {
            try {
                long start = System.currentTimeMillis();
                InetAddress address = InetAddress.getByName(endpoint.getHostname());
                if (address.isReachable((int) TimeUnit.SECONDS.toMillis(4))) { // Check if the endpoint is reachable
                    endpoint.setStatus(EndpointStatus.Status.ONLINE);
                    return;
                }
                // Check if the endpoint took too long to respond
                if (System.currentTimeMillis() - start > TimeUnit.SECONDS.toMillis(2)) {
                    endpoint.setStatus(EndpointStatus.Status.DEGRADED);
                }
            } catch (IOException e) {
                endpoint.setStatus(EndpointStatus.Status.OFFLINE);
            }
        });

        log.info("Fetched Mojang API status for {} endpoints", MOJANG_ENDPOINTS.size());
        CachedEndpointStatus status = new CachedEndpointStatus(
                MOJANG_ENDPOINT_STATUS_KEY,
                MOJANG_ENDPOINTS
        );
        mojangEndpointStatusRepository.save(status);
        status.getCache().setCached(false);
        return status;
    }

    /**
     * Gets the Session Server profile of the
     * player with the given UUID.
     *
     * @param id the uuid or name of the player
     * @return the profile
     */
    public MojangProfileToken getProfile(String id) {
        return WebRequest.getAsEntity(SESSION_SERVER_ENDPOINT + "/session/minecraft/profile/" + id, MojangProfileToken.class);
    }

    /**
     * Gets the UUID of the player using
     * the name of the player.
     *
     * @param id the name of the player
     * @return the profile
     */
    public MojangUsernameToUuidToken getUuidFromUsername(String id) {
        return WebRequest.getAsEntity(API_ENDPOINT + "/users/profiles/minecraft/" + id, MojangUsernameToUuidToken.class);
    }
}
