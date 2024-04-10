package cc.fascinated.service;

import cc.fascinated.common.PlayerUtils;
import cc.fascinated.common.Tuple;
import cc.fascinated.common.UUIDUtils;
import cc.fascinated.exception.impl.MojangAPIRateLimitException;
import cc.fascinated.exception.impl.RateLimitException;
import cc.fascinated.exception.impl.ResourceNotFoundException;
import cc.fascinated.model.cache.CachedPlayer;
import cc.fascinated.model.cache.CachedPlayerName;
import cc.fascinated.model.mojang.MojangProfile;
import cc.fascinated.model.mojang.MojangUsernameToUuid;
import cc.fascinated.model.player.Cape;
import cc.fascinated.model.player.Skin;
import cc.fascinated.repository.PlayerCacheRepository;
import cc.fascinated.repository.PlayerNameCacheRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service @Log4j2
public class PlayerService {

    private final MojangAPIService mojangAPIService;
    private final PlayerCacheRepository playerCacheRepository;
    private final PlayerNameCacheRepository playerNameCacheRepository;

    @Autowired
    public PlayerService(MojangAPIService mojangAPIService, PlayerCacheRepository playerCacheRepository, PlayerNameCacheRepository playerNameCacheRepository) {
        this.mojangAPIService = mojangAPIService;
        this.playerCacheRepository = playerCacheRepository;
        this.playerNameCacheRepository = playerNameCacheRepository;
    }

    /**
     * Get a player from the cache or
     * from the Mojang API.
     *
     * @param id the id of the player
     * @return the player
     */
    public CachedPlayer getPlayer(String id) {
        id = id.toUpperCase(); // Convert the id to uppercase to prevent case sensitivity
        UUID uuid = PlayerUtils.getUuidFromString(id);
        if (uuid == null) { // If the id is not a valid uuid, get the uuid from the username
            uuid = usernameToUuid(id);
        }

        Optional<CachedPlayer> cachedPlayer = playerCacheRepository.findById(uuid);
        if (cachedPlayer.isPresent()) { // Return the cached player if it exists
            return cachedPlayer.get();
        }

        try {
            MojangProfile mojangProfile = mojangAPIService.getProfile(uuid.toString()); // Get the player profile from Mojang
            Tuple<Skin, Cape> skinAndCape = mojangProfile.getSkinAndCape();
            CachedPlayer player = new CachedPlayer(
                    uuid, // Player UUID
                    mojangProfile.getName(), // Player Name
                    skinAndCape.getLeft(), // Skin
                    skinAndCape.getRight(), // Cape
                    System.currentTimeMillis() // Cache time
            );

            playerCacheRepository.save(player);
            player.setCached(-1); // Indicate that the player is not cached
            return player;
        } catch (RateLimitException exception) {
            throw new MojangAPIRateLimitException();
        }
    }

    /**
     * Gets the player's uuid from their username.
     *
     * @param username the username of the player
     * @return the uuid of the player
     */
    private UUID usernameToUuid(String username) {
        Optional<CachedPlayerName> cachedPlayerName = playerNameCacheRepository.findById(username);
        if (cachedPlayerName.isPresent()) {
            return cachedPlayerName.get().getUniqueId();
        }
        try {
            MojangUsernameToUuid mojangUsernameToUuid = mojangAPIService.getUuidFromUsername(username);
            if (mojangUsernameToUuid == null) {
                throw new ResourceNotFoundException("Player with username '%s' not found".formatted(username));
            }
            UUID uuid = UUIDUtils.addDashes(mojangUsernameToUuid.getId());
            playerNameCacheRepository.save(new CachedPlayerName(username, uuid));
            return uuid;
        } catch (RateLimitException exception) {
            throw new MojangAPIRateLimitException();
        }
    }
}
