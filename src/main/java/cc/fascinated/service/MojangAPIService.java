package cc.fascinated.service;

import cc.fascinated.model.mojang.MojangProfile;
import cc.fascinated.model.mojang.MojangUsernameToUuid;
import cc.fascinated.util.WebRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service @Log4j2
public class MojangAPIService {

    @Value("${mojang.session-server}")
    private String mojangSessionServerUrl;

    @Value("${mojang.api}")
    private String mojangApiUrl;

    /**
     * Gets the Session Server profile of the
     * player with the given UUID.
     *
     * @param id the uuid or name of the player
     * @return the profile
     */
    public MojangProfile getProfile(String id) {
        return WebRequest.get(mojangSessionServerUrl + "/session/minecraft/profile/" + id, MojangProfile.class);
    }

    /**
     * Gets the UUID of the player using
     * the name of the player.
     *
     * @param id the name of the player
     * @return the profile
     */
    public MojangUsernameToUuid getUuidFromUsername(String id) {
        return WebRequest.get(mojangApiUrl + "/users/profiles/minecraft/" + id, MojangUsernameToUuid.class);
    }
}
