package cc.fascinated.player.impl;

import cc.fascinated.Main;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
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
        this.head = new SkinPart(this.skinBytes, 8, 8, 8, 8, 20);
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
}
