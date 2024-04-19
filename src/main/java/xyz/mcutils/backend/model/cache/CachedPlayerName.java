package xyz.mcutils.backend.model.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import xyz.mcutils.backend.common.CachedResponse;

import java.util.UUID;

/**
 * @author Braydon
 */
@Setter
@Getter @EqualsAndHashCode(callSuper = false)
@RedisHash(value = "playerName", timeToLive = 60L * 60L * 6) // 6 hours (in seconds)
public class CachedPlayerName extends CachedResponse {
    /**
     * The id of the player.
     */
    @JsonIgnore
    @Id private final String id;

    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The unique id of the player.
     */
    private final UUID uniqueId;

    public CachedPlayerName(String id, String username, UUID uniqueId) {
        super(Cache.defaultCache());
        this.id = id;
        this.username = username;
        this.uniqueId = uniqueId;
    }
}