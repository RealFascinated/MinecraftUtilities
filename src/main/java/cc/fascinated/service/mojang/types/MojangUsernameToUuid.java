package cc.fascinated.service.mojang.types;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class MojangUsernameToUuid {

    /**
     * The UUID of the player.
     */
    private String id;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * Check if the profile is valid.
     *
     * @return if the profile is valid
     */
    public boolean isValid() {
        return id != null && name != null;
    }
}
