package cc.fascinated.service.mojang.model;

import cc.fascinated.Main;
import cc.fascinated.model.player.Cape;
import cc.fascinated.model.player.Skin;
import cc.fascinated.util.Tuple;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor
public class MojangProfile {

    /**
     * The UUID of the player.
     */
    private String id;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * The properties of the player.
     */
    private final List<ProfileProperty> properties = new ArrayList<>();
    
    /**
     * Get the skin and cape of the player.
     *
     * @return the skin and cape of the player
     */
    public Tuple<Skin, Cape> getSkinAndCape() {
        ProfileProperty textureProperty = getTextureProperty();
        if (textureProperty == null) {
            return null;
        }

        // Decode the texture property
        String decoded = new String(java.util.Base64.getDecoder().decode(textureProperty.getValue()));

        // Parse the decoded JSON
        JsonObject json = Main.getGSON().fromJson(decoded, JsonObject.class);
        JsonObject texturesJson = json.getAsJsonObject("textures");
        JsonObject skinJson = texturesJson.getAsJsonObject("SKIN");
        JsonObject capeJson = texturesJson.getAsJsonObject("CAPE");
        JsonObject metadataJson = skinJson.get("metadata").getAsJsonObject();

        Skin skin = new Skin(id, skinJson.get("url").getAsString(),
                Skin.SkinType.valueOf(metadataJson.get("model").getAsString().toUpperCase()));
        Cape cape = new Cape(capeJson.get("url").getAsString());

        return new Tuple<>(skin, cape);
    }
    
    /**
     * Get the texture property of the player.
     *
     * @return the texture property
     */
    public ProfileProperty getTextureProperty() {
        for (ProfileProperty property : properties) {
            if (property.getName().equals("textures")) {
                return property;
            }
        }
        return null;
    }

    @Getter @AllArgsConstructor
    public static class ProfileProperty {
        /**
         * The name of the property.
         */
        private String name;

        /**
         * The base64 value of the property.
         */
        private String value;

        /**
         * The signature of the property.
         */
        private String signature;

        /**
         * Check if the property is signed.
         *
         * @return true if the property is signed, false otherwise
         */
        public boolean isSigned() {
            return signature != null;
        }
    }
}
