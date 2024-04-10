package cc.fascinated.model.cache;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@AllArgsConstructor
@RedisHash(value = "player", timeToLive = 60L * 60L) // 1 hour (in seconds)
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
