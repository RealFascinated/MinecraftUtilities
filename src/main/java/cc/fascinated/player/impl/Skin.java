package cc.fascinated.player.impl;

import cc.fascinated.Main;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
     * The head of the skin
     */
    @JsonIgnore
    private final SkinPart head;

    public Skin(String url, SkinType model) {
        this.url = url;
        this.model = model;
        this.skinBytes = this.getSkinData();

        // The skin parts
        this.head = new SkinPart(this.skinBytes, SkinPartEnum.HEAD);
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
}
