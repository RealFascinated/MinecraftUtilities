package cc.fascinated.model.mojang;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class MojangUsernameToUuid {

    /**
     * The UUID of the player.
     */
    @JsonProperty("id")
    private String uuid;

    /**
     * The name of the player.
     */
    @JsonProperty("name")
    private String username;

    /**
     * Check if the profile is valid.
     *
     * @return if the profile is valid
     */
    public boolean isValid() {
        return uuid != null && username != null;
    }
}
