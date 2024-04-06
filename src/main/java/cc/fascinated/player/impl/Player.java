package cc.fascinated.player.impl;

import cc.fascinated.Main;
import cc.fascinated.mojang.types.MojangSessionServerProfile;
import cc.fascinated.mojang.types.MojangSessionServerProfileProperties;
import cc.fascinated.util.UUIDUtils;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {

    /**
     * The UUID of the player
     */
    private final UUID uuid;

    /**
     * The name of the player
     */
    private final String name;

    /**
     * The skin of the player
     * <p>
     *     This will be null if the player does not have a skin.
     * </p>
     */
    private Skin skin;

    public Player(MojangSessionServerProfile profile) {
        this.uuid = UUID.fromString(UUIDUtils.addUUIDDashes(profile.getId()));
        this.name = profile.getName();

        MojangSessionServerProfileProperties textureProperty = profile.getTextureProperty();
        if (textureProperty == null) {
            return;
        }

        // Decode the texture property
        String decoded = new String(java.util.Base64.getDecoder().decode(textureProperty.getValue()));

        // Parse the decoded JSON
        JsonObject json = Main.getGSON().fromJson(decoded, JsonObject.class);
        JsonObject textures = json.getAsJsonObject("textures");
        JsonObject skin = textures.getAsJsonObject("SKIN");
        JsonObject metadata = skin.get("metadata").getAsJsonObject();

        String url = skin.get("url").getAsString();
        SkinType model = SkinType.fromString(metadata.get("model").getAsString());

        this.skin = new Skin(url, model);
    }

}
