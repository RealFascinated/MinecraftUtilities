package cc.fascinated.model.player;

import cc.fascinated.common.PlayerUtils;
import cc.fascinated.config.Config;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.service.skin.SkinRenderer;
import cc.fascinated.service.skin.impl.BodyRenderer;
import cc.fascinated.service.skin.impl.HeadRenderer;
import cc.fascinated.service.skin.impl.IsometricHeadRenderer;
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
        FACE(new HeadRenderer()),
        HEAD(new IsometricHeadRenderer()),
        BODY(new BodyRenderer());

        /**
         * The skin part renderer for the part.
         */
        private final SkinRenderer skinRenderer;

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

    @AllArgsConstructor @Getter
    public enum PartPosition {
        /**
         * Skin postions
         */
        HEAD(8, 8, 8, 8, false),
        HEAD_TOP(8, 0, 8, 8, false),
        HEAD_FRONT(8, 8, 8, 8, false),
        HEAD_RIGHT(0, 8, 8, 8, false),

        BODY(20, 20, 8, 12, false),
        BODY_BACK(20, 36, 8, 12, false),
        BODY_LEFT(32, 52, 8, 12, false),
        BODY_RIGHT(44, 20, 8, 12, false),

        RIGHT_ARM(44, 20, 4, 12, false),
        LEFT_ARM(36, 52, 4, 12, true),

        RIGHT_LEG(4, 20, 4, 12, false),
        LEFT_LEG(20, 52, 4, 12, true),

        /**
         * Skin overlay (layer) positions
         */
        HEAD_OVERLAY_TOP(40, 0, 8, 8, false),
        HEAD_OVERLAY_FRONT(40, 8, 8, 8, false),
        HEAD_OVERLAY_RIGHT(32, 8, 8, 8, false),
        HEAD_OVERLAY_LEFT(48, 8, 8, 8, false),

        BODY_OVERLAY_FRONT(20, 36, 8, 12, false),

        RIGHT_ARM_OVERLAY(44, 36, 8, 12, false),
        LEFT_ARM_OVERLAY(52, 52, 8, 12, false),

        RIGHT_LEG_OVERLAY(4, 36, 4, 12, false),
        LEFT_LEG_OVERLAY(4, 52, 20, 12, false);

        /**
         * The x, and y position of the part.
         */
        private final int x, y;

        /**
         * The width and height of the part.
         */
        private final int width, height;

        /*
         * Should the part be flipped horizontally?
         */
        private final boolean flipped;
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
