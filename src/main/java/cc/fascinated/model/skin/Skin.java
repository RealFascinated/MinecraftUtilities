package cc.fascinated.model.skin;

import cc.fascinated.common.PlayerUtils;
import cc.fascinated.config.Config;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor @NoArgsConstructor
@Getter @Log4j2
public class Skin {
    /**
     * The default skin, usually used when the skin is not found.
     */
    public static final Skin DEFAULT_SKIN = new Skin("http://textures.minecraft.net/texture/60a5bd016b3c9a1b9272e4929e30827a67be4ebb219017adbbc4a4d22ebd5b1",
            Model.DEFAULT);

    /**
     * The URL for the skin
     */
    private String url;

    /**
     * The model for the skin
     */
    private Model model;

    /**
     * The legacy status of the skin
     */
    private boolean isLegacy = false;

    /**
     * The skin image for the skin
     */
    @JsonIgnore
    private byte[] skinImage;

    /**
     * The part URLs of the skin
     */
    @JsonProperty("parts")
    private Map<String, String> partUrls = new HashMap<>();

    public Skin(String url, Model model) {
        this.url = url;
        this.model = model;

        this.skinImage = PlayerUtils.getSkinImage(url);
        if (this.skinImage != null) {
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(this.skinImage));
                if (image.getWidth() == 64 && image.getHeight() == 32) { // Using the old skin format
                    this.isLegacy = true;
                }
            } catch (Exception ignored) {}
        }
    }

    /**
     * Gets the skin from a {@link JsonObject}.
     *
     * @param json the JSON object
     * @return the skin
     */
    public static Skin fromJson(JsonObject json) {
        if (json == null) {
            return null;
        }
        String url = json.get("url").getAsString();
        JsonObject metadata = json.getAsJsonObject("metadata");
        Model model = Model.fromName(metadata == null ? "default" : // Fall back to slim if the model is not found
                metadata.get("model").getAsString());
        return new Skin(url, model);
    }

    /**
     * Populates the part URLs for the skin.
     *
     * @param playerUuid the player's UUID
     */
    public Skin populatePartUrls(String playerUuid) {
        for (Enum<?>[] type : ISkinPart.TYPES) {
            for (Enum<?> part : type) {
                ISkinPart skinPart = (ISkinPart) part;
                if (skinPart.hidden()) {
                    continue;
                }
                String partName = part.name().toLowerCase();
                this.partUrls.put(partName, Config.INSTANCE.getWebPublicUrl() + "/player/" + partName + "/" + playerUuid);
            }
        }
        return this;
    }

    /**
     * The model of the skin.
     */
    public enum Model {
        DEFAULT,
        SLIM;

        /**
         * Gets the model from its name.
         *
         * @param name the name of the model
         * @return the model
         */
        public static Model fromName(String name) {
            for (Model model : values()) {
                if (model.name().equalsIgnoreCase(name)) {
                    return model;
                }
            }
            return null;
        }
    }
}
