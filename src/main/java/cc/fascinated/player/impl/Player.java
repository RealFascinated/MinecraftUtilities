package cc.fascinated.player.impl;

import cc.fascinated.Consts;
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
     * The avatar URL of the player
     */
    private final String avatarUrl;

    /**
     * The skin of the player
     * <p>
     *     This will be null if the player does not have a skin.
     * </p>
     */
    private Skin skin;

    /**
     * The cape of the player
     * <p>
     *     This will be null if the player does not have a cape.
     * </p>
     */
    private Cape cape;

    public Player(MojangSessionServerProfile profile) {
        this.uuid = UUID.fromString(UUIDUtils.addUUIDDashes(profile.getId()));
        this.name = profile.getName();
        this.avatarUrl = Consts.getSITE_URL() + "/avatar/" + this.uuid;

        MojangSessionServerProfileProperties textureProperty = profile.getTextureProperty();
        if (textureProperty == null) {
            return;
        }

        // Decode the texture property
        String decoded = new String(java.util.Base64.getDecoder().decode(textureProperty.getValue()));

        // Parse the decoded JSON
        JsonObject json = Main.getGSON().fromJson(decoded, JsonObject.class);
        JsonObject texturesJson = json.getAsJsonObject("textures");
        JsonObject skinJson = texturesJson.getAsJsonObject("SKIN");
        JsonObject capeJson = texturesJson.getAsJsonObject("CAPE");
        JsonObject metadataJson = skinJson.get("metadata").getAsJsonObject();

        this.skin = new Skin(skinJson.get("url").getAsString(), SkinType.fromString(metadataJson.get("model").getAsString()));
        this.cape = new Cape(capeJson.get("url").getAsString());
    }
}
