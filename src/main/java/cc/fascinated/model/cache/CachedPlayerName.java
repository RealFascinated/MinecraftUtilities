package cc.fascinated.model.cache;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

/**
 * @author Braydon
 */
@Getter
@ToString
@RedisHash(value = "playerName", timeToLive = 60L * 60L * 6) // 6 hours (in seconds)
public final class CachedPlayerName extends CachedResponse {
    /**
     * The username of the player.
     */
    @Id private final String username;

    /**
     * The unique id of the player.
     */
    private final UUID uniqueId;

    public CachedPlayerName(String username, UUID uniqueId) {
        super(CacheInformation.defaultCache());
        this.username = username;
        this.uniqueId = uniqueId;
    }
}