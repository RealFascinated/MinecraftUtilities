package cc.fascinated.model.player;

import cc.fascinated.Main;
import cc.fascinated.config.Config;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Getter @Log4j2
public class Skin {

    /**
     * The URL of the skin
     */
    private final String url;

    /**
     * The model of the skin
     */
    private final SkinType model;

    /**
     * The bytes of the skin
     */
    @JsonIgnore
    private final byte[] skinBytes;

    /**
     * The skin parts for this skin
     */
    @JsonIgnore
    private final Map<SkinPartEnum, SkinPart> parts = new HashMap<>();

    @JsonProperty("parts")
    private final Map<String, String> partUrls = new HashMap<>();

    public Skin(String playerUuid, String url, SkinType model) {
        this.url = url;
        this.model = model;
        this.skinBytes = this.getSkinData();

        // The skin parts
        this.parts.put(SkinPartEnum.HEAD, new SkinPart(this.skinBytes, SkinPartEnum.HEAD));

        for (Map.Entry<SkinPartEnum, SkinPart> entry : this.parts.entrySet()) {
            String partName = entry.getKey().name().toLowerCase();
            this.partUrls.put(partName, Config.INSTANCE.getWebPublicUrl() + "/player/" + partName + "/" + playerUuid + "?size=250");
        }
    }

    /**
     * Gets the default/fallback head.
     *
     * @return the default head
     */
    public static SkinPart getDefaultHead() {
        try (InputStream stream = Main.class.getClassLoader().getResourceAsStream("images/default_head.png")) {
            if (stream == null) {
                return null;
            }
            byte[] bytes = stream.readAllBytes();
            return new SkinPart(bytes, SkinPartEnum.HEAD);
        } catch (Exception ex) {
            log.warn("Failed to load default head", ex);
            return null;
        }
    }

    /**
     * Gets the skin data from the URL.
     *
     * @return the skin data
     */
    @SneakyThrows @JsonIgnore
    public byte[] getSkinData() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(this.url))
                .GET()
                .build();

        return Main.getCLIENT().send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
    }

    /**
     * Gets a part from the skin.
     *
     * @param part the part name
     * @return the part
     */
    public SkinPart getPart(String part) {
        return this.parts.get(SkinPartEnum.valueOf(part.toUpperCase()));
    }

    /**
     * The skin part enum that contains the
     * information about the part.
     */
    @Getter @AllArgsConstructor
    public enum SkinPartEnum {

        HEAD(8, 8, 8, 8, 250);

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
    }

    /**
     * The type of the skin.
     */
    public enum SkinType {
        DEFAULT,
        SLIM
    }
}
