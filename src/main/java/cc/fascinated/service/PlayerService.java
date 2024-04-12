package cc.fascinated.service;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.common.PlayerUtils;
import cc.fascinated.common.Tuple;
import cc.fascinated.common.UUIDUtils;
import cc.fascinated.config.Config;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.exception.impl.MojangAPIRateLimitException;
import cc.fascinated.exception.impl.RateLimitException;
import cc.fascinated.exception.impl.ResourceNotFoundException;
import cc.fascinated.model.cache.CachedPlayer;
import cc.fascinated.model.cache.CachedPlayerName;
import cc.fascinated.model.cache.CachedPlayerSkinPart;
import cc.fascinated.model.mojang.MojangProfile;
import cc.fascinated.model.mojang.MojangUsernameToUuid;
import cc.fascinated.model.player.Cape;
import cc.fascinated.model.player.Player;
import cc.fascinated.model.skin.ISkinPart;
import cc.fascinated.model.skin.Skin;
import cc.fascinated.repository.PlayerCacheRepository;
import cc.fascinated.repository.PlayerNameCacheRepository;
import cc.fascinated.repository.PlayerSkinPartCacheRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.UUID;

@Service @Log4j2
public class PlayerService {

    private final MojangService mojangAPIService;
    private final PlayerCacheRepository playerCacheRepository;
    private final PlayerNameCacheRepository playerNameCacheRepository;
    private final PlayerSkinPartCacheRepository playerSkinPartCacheRepository;

    @Autowired
    public PlayerService(MojangService mojangAPIService, PlayerCacheRepository playerCacheRepository,
                         PlayerNameCacheRepository playerNameCacheRepository, PlayerSkinPartCacheRepository playerSkinPartCacheRepository) {
        this.mojangAPIService = mojangAPIService;
        this.playerCacheRepository = playerCacheRepository;
        this.playerNameCacheRepository = playerNameCacheRepository;
        this.playerSkinPartCacheRepository = playerSkinPartCacheRepository;
    }

    /**
     * Get a player from the cache or
     * from the Mojang API.
     *
     * @param id the id of the player
     * @return the player
     */
    public CachedPlayer getPlayer(String id) {
        // Convert the id to uppercase to prevent case sensitivity
        log.info("Getting player: {}", id);
        UUID uuid = PlayerUtils.getUuidFromString(id);
        if (uuid == null) { // If the id is not a valid uuid, get the uuid from the username
            uuid = usernameToUuid(id).getUniqueId();
        }

        Optional<CachedPlayer> cachedPlayer = playerCacheRepository.findById(uuid);
        if (cachedPlayer.isPresent() && Config.INSTANCE.isProduction()) { // Return the cached player if it exists
            log.info("Player {} is cached", id);
            return cachedPlayer.get();
        }

        try {
            log.info("Getting player profile from Mojang: {}", id);
            MojangProfile mojangProfile = mojangAPIService.getProfile(uuid.toString()); // Get the player profile from Mojang
            log.info("Got player profile from Mojang: {}", id);
            Tuple<Skin, Cape> skinAndCape = mojangProfile.getSkinAndCape();
            CachedPlayer player = new CachedPlayer(
                    uuid, // Player UUID
                    UUIDUtils.removeDashes(uuid), // Trimmed UUID
                    mojangProfile.getName(), // Player Name
                    skinAndCape.getLeft(), // Skin
                    skinAndCape.getRight(), // Cape
                    mojangProfile.getProperties(), // Raw properties
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
    public CachedPlayerName usernameToUuid(String username) {
        log.info("Getting UUID from username: {}", username);
        Optional<CachedPlayerName> cachedPlayerName = playerNameCacheRepository.findById(username.toUpperCase());
        if (cachedPlayerName.isPresent() && Config.INSTANCE.isProduction()) {
            return cachedPlayerName.get();
        }
        try {
            MojangUsernameToUuid mojangUsernameToUuid = mojangAPIService.getUuidFromUsername(username);
            if (mojangUsernameToUuid == null) {
                log.info("Player with username '{}' not found", username);
                throw new ResourceNotFoundException("Player with username '%s' not found".formatted(username));
            }
            UUID uuid = UUIDUtils.addDashes(mojangUsernameToUuid.getUuid());
            CachedPlayerName player = new CachedPlayerName(username, uuid);
            playerNameCacheRepository.save(player);
            log.info("Got UUID from username: {} -> {}", username, uuid);
            return player;
        } catch (RateLimitException exception) {
            throw new MojangAPIRateLimitException();
        }
    }

    /**
     * Gets a skin part from the player's skin.
     *
     * @param player the player
     * @param partName the name of the part
     * @param renderOverlay whether to render the overlay
     * @return the skin part
     */
    public CachedPlayerSkinPart getSkinPart(Player player, String partName, boolean renderOverlay, int size) {
        if (size > 512) {
            log.info("Size {} is too large, setting to 512", size);
            size = 512;
        }
        if (size < 32) {
            log.info("Size {} is too small, setting to 32", size);
            size = 32;
        }

        ISkinPart part = ISkinPart.getByName(partName); // The skin part to get
        if (part == null) {
            throw new BadRequestException("Invalid skin part: %s".formatted(partName));
        }

        String name = part.name();
        log.info("Getting skin part {} for player: {} (size: {}, renderOverlays: {})", name, player.getUniqueId(), size, renderOverlay);
        String key = "%s-%s-%s-%s".formatted(player.getUniqueId(), name, size, renderOverlay);

        Optional<CachedPlayerSkinPart> cache = playerSkinPartCacheRepository.findById(key);

        // The skin part is cached
        if (cache.isPresent() && Config.INSTANCE.isProduction()) {
            log.info("Skin part {} for player {} is cached", name, player.getUniqueId());
            return cache.get();
        }

        long before = System.currentTimeMillis();
        BufferedImage renderedPart = part.render(player.getSkin(), renderOverlay, size); // Render the skin part
        log.info("Took {}ms to render skin part {} for player: {}", System.currentTimeMillis() - before, name, player.getUniqueId());

        CachedPlayerSkinPart skinPart = new CachedPlayerSkinPart(
                key,
                ImageUtils.imageToBytes(renderedPart)
        );
        log.info("Fetched skin part {} for player: {}", name, player.getUniqueId());
        playerSkinPartCacheRepository.save(skinPart);
        return skinPart;
    }
}
