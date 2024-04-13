package xyz.mcutils.backend.model.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@AllArgsConstructor
@RedisHash(value = "playerSkinPart", timeToLive = 60L * 60L) // 1 hour (in seconds)
public class CachedPlayerSkinPart {

    /**
     * The ID of the skin part
     */
    @Id @NonNull private String id;

    /**
     * The skin part bytes
     */
    private byte[] bytes;
}
