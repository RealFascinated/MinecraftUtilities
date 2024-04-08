package cc.fascinated.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UUIDUtils {

    /**
     * Add dashes to a UUID.
     *
     * @param idNoDashes the UUID without dashes
     * @return the UUID with dashes
     */
    public static String addUUIDDashes(String idNoDashes) {
        StringBuilder idBuff = new StringBuilder(idNoDashes);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return idBuff.toString();
    }
}
