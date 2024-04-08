package cc.fascinated.player;

import cc.fascinated.mojang.MojangAPIService;
import cc.fascinated.mojang.types.MojangApiProfile;
import cc.fascinated.mojang.types.MojangSessionServerProfile;
import cc.fascinated.player.impl.Player;
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
        if (id.length() == 32 || id.length() == 36) {
            try {
                uuid = UUID.fromString(id.length() == 32 ? UUIDUtils.addUUIDDashes(id) : id);
            } catch (Exception ignored) {}
        } else {
            uuid = playerNameToUUIDCache.get(id.toUpperCase());
        }

        if (uuid != null && players.containsKey(uuid)) {
            return players.get(uuid);
        }

        MojangSessionServerProfile profile = uuid == null ? null : mojangAPIService.getSessionServerProfile(uuid.toString());
        if (profile == null) {
            MojangApiProfile apiProfile = mojangAPIService.getApiProfile(id);
            if (apiProfile == null || !apiProfile.isValid()) {
                return null;
            }
            profile = mojangAPIService.getSessionServerProfile(apiProfile.getId().length() == 32 ? UUIDUtils.addUUIDDashes(apiProfile.getId()) : apiProfile.getId());
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
