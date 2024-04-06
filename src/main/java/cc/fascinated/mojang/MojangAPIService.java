package cc.fascinated.mojang;

import cc.fascinated.mojang.types.MojangApiProfile;
import cc.fascinated.mojang.types.MojangSessionServerProfile;
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
     * Gets the Session Server profile of the player with the given UUID.
     *
     * @param id the uuid or name of the player
     * @return the profile
     */
    public MojangSessionServerProfile getSessionServerProfile(String id) {
        return WebRequest.get(mojangSessionServerUrl + "/session/minecraft/profile/" + id);
    }

    /**
     * Gets the Mojang API profile of the player with the given UUID.
     *
     * @param id the name of the player
     * @return the profile
     */
    public MojangApiProfile getApiProfile(String id) {
        return WebRequest.get(mojangApiUrl + "/users/profiles/minecraft/" + id);
    }
}
