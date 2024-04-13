package cc.fascinated.model.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class CachedResponse {

    /**
     * The cache information for this response.
     */
    private CacheInformation cache;
}
