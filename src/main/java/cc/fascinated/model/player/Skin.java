package cc.fascinated.model.player;

import cc.fascinated.common.PlayerUtils;
import cc.fascinated.config.Config;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

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
    private final String url;

    /**
     * The model for the skin
     */
    private final Model model;

    /**
     * The skin image for the skin
     */
    @JsonIgnore
    private final BufferedImage skinImage;

    /**
     * The part URLs of the skin
     */
    @JsonProperty("parts")
    private final Map<String, String> partUrls = new HashMap<>();

    public Skin(String url, Model model) {
        this.url = url;
        this.model = model;

        this.skinImage = PlayerUtils.getSkinImage(url);
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
        Model model = Model.fromName(metadata == null ? "slim" : // Fall back to slim if the model is not found
                metadata.get("model").getAsString());
        return new Skin(url, model);
    }

    /**
     * Populates the part URLs for the skin.
     *
     * @param playerUuid the player's UUID
     */
    public Skin populatePartUrls(String playerUuid) {
        for (Parts part : Parts.values()) {
            String partName = part.name().toLowerCase();
            this.partUrls.put(partName, Config.INSTANCE.getWebPublicUrl() + "/player/" + partName + "/" + playerUuid + "?size=" + part.getDefaultSize());
        }
        return this;
    }

    /**
     * The skin part enum that contains the
     * information about the part.
     */
    @Getter @AllArgsConstructor
    public enum Parts {

        HEAD(8, 8, 8, 8, 256);

        /**
         * The x and y position of the part.
         */
        private final int x, y;

        /**
         * The width and height of the part.
         */
        private final int width, height;

        /**
         * The scale of the part.
         */
        private final int defaultSize;

        /**
         * Gets the name of the part.
         *
         * @return the name of the part
         */
        public String getName() {
            return this.name().toLowerCase();
        }

        /**
         * Gets the skin part from its name.
         *
         * @param name the name of the part
         * @return the skin part
         */
        public static Parts fromName(String name) {
            for (Parts part : values()) {
                if (part.name().equalsIgnoreCase(name)) {
                    return part;
                }
            }
            return null;
        }
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
