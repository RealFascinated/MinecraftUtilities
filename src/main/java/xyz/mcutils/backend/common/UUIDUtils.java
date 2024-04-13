package xyz.mcutils.backend.common;

import io.micrometer.common.lang.NonNull;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDUtils {

    /**
     * Add dashes to a UUID.
     *
     * @param trimmed the UUID without dashes
     * @return the UUID with dashes
     */
    @NonNull
    public static UUID addDashes(@NonNull String trimmed) {
        StringBuilder builder = new StringBuilder(trimmed);
        for (int i = 0, pos = 20; i < 4; i++, pos -= 4) {
            builder.insert(pos, "-");
        }
        return UUID.fromString(builder.toString());
    }

    /**
     * Remove dashes from a UUID.
     *
     * @param dashed the UUID with dashes
     * @return the UUID without dashes
     */
    @NonNull
    public static String removeDashes(@NonNull UUID dashed) {
        return dashed.toString().replace("-", "");
    }
}
