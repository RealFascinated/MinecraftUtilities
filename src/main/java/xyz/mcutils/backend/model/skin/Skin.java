package xyz.mcutils.backend.model.skin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import xyz.mcutils.backend.common.EnumUtils;
import xyz.mcutils.backend.common.PlayerUtils;
import xyz.mcutils.backend.config.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor @NoArgsConstructor
@Getter @Log4j2
public class Skin {
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
    private boolean legacy;

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
                this.legacy = image.getWidth() == 64 && image.getHeight() == 32;
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
        return new Skin(
                url,
                EnumUtils.getEnumConstant(Model.class, metadata != null ? metadata.get("model").getAsString().toUpperCase() : "DEFAULT")
        );
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
        SLIM
    }
}
