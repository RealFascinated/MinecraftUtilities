package cc.fascinated.mojang.types;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @ToString
public class MojangSessionServerProfile {

    /**
     * The UUID of the player.
     */
    private String id;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * The properties for the player.
     */
    private final List<MojangSessionServerProfileProperties> properties = new ArrayList<>();

    public MojangSessionServerProfile() {}

    /**
     * Get the texture property for the player.
     *
     * @return the texture property
     */
    public MojangSessionServerProfileProperties getTextureProperty() {
        for (MojangSessionServerProfileProperties property : properties) {
            if (property.getName().equals("textures")) {
                return property;
            }
        }
        return null;
    }
}
