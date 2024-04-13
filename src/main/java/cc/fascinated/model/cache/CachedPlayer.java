package cc.fascinated.model.cache;

import cc.fascinated.model.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

/**
 * A cacheable {@link Player}.
 *
 * @author Braydon
 */
@Setter @Getter
@AllArgsConstructor
@RedisHash(value = "player", timeToLive = 60L * 60L) // 1 hour (in seconds)
public final class CachedPlayer implements Serializable {
    /**
     * The unique id of the player.
     */
    @JsonIgnore
    @Id private UUID uniqueId;

    /**
     * The player to cache.
     */
    private Player player;

    /**
     * The cache information about the request.
     */
    private CacheInformation cache;
}