package cc.fascinated.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class CacheInformation {
    /**
     * Whether this request is cached.
     */
    private boolean cached;

    /**
     * The unix timestamp of when this was cached.
     */
    private long cachedTime;

    /**
     * Create a new cache information object with the default values.
     * <p>
     *     The default values are:
     *     <br>
     *     <ul>
     *         <li>cached: true</li>
     *         <li>cachedAt: {@link System#currentTimeMillis()}</li>
     *     </ul>
     *     <br>
     * </p>
     *
     * @return the default cache information object
     */
    public static CacheInformation defaultCache() {
        return new CacheInformation(true, System.currentTimeMillis());
    }

    /**
     * Sets if this request is cached.
     *
     * @param cached the new value of if this request is cached
     */
    public void setCached(boolean cached) {
        this.cached = cached;
        if (!cached) {
            cachedTime = -1;
        }
    }
}
