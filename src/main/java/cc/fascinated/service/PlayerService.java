package cc.fascinated.service;

import cc.fascinated.service.mojang.MojangAPIService;
import cc.fascinated.service.mojang.model.MojangProfile;
import cc.fascinated.service.mojang.model.MojangUsernameToUuid;
import cc.fascinated.model.player.Player;
import cc.fascinated.util.UUIDUtils;
import lombok.extern.log4j.Log4j2;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service @Log4j2
public class PlayerService {

    /**
     * The cache of players.
     */
    private final Map<UUID, Player> players = ExpiringMap.builder()
            .expiration(1, TimeUnit.HOURS)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .build();

    /**
     * The cache of player names to UUIDs.
     */
    private final Map<String, UUID> playerNameToUUIDCache = ExpiringMap.builder()
            .expiration(1, TimeUnit.DAYS)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .build();

    private final MojangAPIService mojangAPIService;

    public PlayerService(MojangAPIService mojangAPIService) {
        this.mojangAPIService = mojangAPIService;
    }

    /**
     * Gets a player by their UUID.
     *
     * @param id the uuid or name of the player
     * @return the player or null if the player does not exist
     */
    public Player getPlayer(String id) {
        UUID uuid = null;
        if (id.length() == 32 || id.length() == 36) { // Check if the id is a UUID
            try {
                uuid = UUID.fromString(id.length() == 32 ? UUIDUtils.addUUIDDashes(id) : id);
            } catch (Exception ignored) {}
        } else { // Check if the id is a name
            uuid = playerNameToUUIDCache.get(id.toUpperCase());
        }

        // Check if the player is cached
        if (uuid != null && players.containsKey(uuid)) {
            return players.get(uuid);
        }

        MojangProfile profile = uuid == null ? null : mojangAPIService.getProfile(uuid.toString());
        if (profile == null) { // The player cannot be found using their UUID
            MojangUsernameToUuid apiProfile = mojangAPIService.getUuidFromUsername(id); // Get the UUID of the player using their name
            if (apiProfile == null || !apiProfile.isValid()) {
                return null;
            }
            // Get the profile of the player using their UUID
            profile = mojangAPIService.getProfile(apiProfile.getId().length() == 32 ?
                    UUIDUtils.addUUIDDashes(apiProfile.getId()) : apiProfile.getId());
        }
        if (profile == null) { // The player cannot be found using their name or UUID
            log.info("Player with id {} could not be found", id);
            return null;
        }
        Player player = new Player(profile);
        players.put(player.getUuid(), player);
        playerNameToUUIDCache.put(player.getName().toUpperCase(), player.getUuid());
        return player;
    }
}
