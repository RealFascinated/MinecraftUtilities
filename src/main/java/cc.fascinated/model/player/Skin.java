package cc.fascinated.model.player;

import cc.fascinated.common.PlayerUtils;
import cc.fascinated.config.Config;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.service.skin.SkinPartParser;
import cc.fascinated.service.skin.impl.FlatParser;
import cc.fascinated.service.skin.impl.IsometricHeadParser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
            this.partUrls.put(partName, Config.INSTANCE.getWebPublicUrl() + "/player/" + partName + "/" + playerUuid);
        }
        return this;
    }

    /**
     * The skin part enum that contains the
     * information about the part.
     */
    @Getter @AllArgsConstructor
    public enum Parts {

        /**
         * Head parts
         */
        HEAD(new FlatParser(8, 8, 8)),
        HEAD_ISOMETRIC(new IsometricHeadParser());

        /**
         * The skin part parser for the part.
         */
        private final SkinPartParser skinPartParser;

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
         * @throws BadRequestException if the part is not found
         */
        public static Parts fromName(String name) throws BadRequestException {
            for (Parts part : values()) {
                if (part.name().equalsIgnoreCase(name)) {
                    return part;
                }
            }
            throw new BadRequestException("Invalid part name: " + name);
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
