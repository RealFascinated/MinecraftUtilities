package cc.fascinated.model.cache;

import cc.fascinated.model.mojang.MojangProfile;
import cc.fascinated.model.player.Cape;
import cc.fascinated.model.player.Player;
import cc.fascinated.model.player.Skin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

/**
 * A cacheable {@link Player}.
 *
 * @author Braydon
 */
@Setter @Getter
@ToString(callSuper = true)
@RedisHash(value = "player", timeToLive = 60L * 60L) // 1 hour (in seconds)
public final class CachedPlayer extends Player implements Serializable {
    /**
     * The unix timestamp of when this
     * player was cached, -1 if not cached.
     */
    private long cached;

    public CachedPlayer(UUID uuid, String username, Skin skin, Cape cape, MojangProfile.ProfileProperty[] rawProperties, long cached) {
        super(uuid, username, skin, cape, rawProperties);
        this.cached = cached;
    }
}