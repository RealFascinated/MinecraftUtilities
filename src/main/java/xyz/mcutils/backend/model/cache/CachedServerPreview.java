package xyz.mcutils.backend.model.cache;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Setter @Getter @EqualsAndHashCode
@RedisHash(value = "serverPreview", timeToLive = 60L * 5) // 5 minutes (in seconds)
public class CachedServerPreview {

    /**
     * The ID of the server preview
     */
    @Id @NonNull private String id;

    /**
     * The server preview bytes
     */
    private byte[] bytes;
}
