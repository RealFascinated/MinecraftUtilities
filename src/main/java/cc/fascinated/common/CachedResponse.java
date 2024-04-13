package cc.fascinated.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class CachedResponse {

    /**
     * The cache information for this response.
     */
    private Cache cache;

    @AllArgsConstructor @Getter @Setter
    public static class Cache {
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
        public static Cache defaultCache() {
            return new Cache(true, System.currentTimeMillis());
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
}
