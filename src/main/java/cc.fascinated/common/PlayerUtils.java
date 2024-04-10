package cc.fascinated.common;

import cc.fascinated.Main;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.model.player.Skin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@UtilityClass @Log4j2
public class PlayerUtils {

    /**
     * Gets the UUID from the string.
     *
     * @param id the id string
     * @return the UUID
     */
    public static UUID getUuidFromString(String id) {
        UUID uuid;
        boolean isFullUuid = id.length() == 36;
        if (id.length() == 32 || isFullUuid) {
            try {
                uuid = isFullUuid ? UUID.fromString(id) : UUIDUtils.addDashes(id);
            } catch (IllegalArgumentException exception) {
                throw new BadRequestException("Invalid UUID provided: %s".formatted(id));
            }
            return uuid;
        }
        return null;
    }

    /**
     * Gets the skin data from the URL.
     *
     * @return the skin data
     */
    @SneakyThrows
    @JsonIgnore
    public static byte[] getSkinImage(String url) {
        HttpResponse<byte[]> response = Main.HTTP_CLIENT.send(HttpRequest.newBuilder(URI.create(url)).build(),
                HttpResponse.BodyHandlers.ofByteArray());
        return response.body();
    }

    /**
     * Gets the part data from the skin.
     *
     * @return the part data
     */
    public static byte[] getSkinPartBytes(Skin skin, Skin.Parts part, int size) {
        if (size == -1) {
            size = part.getDefaultSize();
        }

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(skin.getSkinImage()));
            if (image == null) {
                image = ImageIO.read(new ByteArrayInputStream(Skin.DEFAULT_SKIN.getSkinImage())); // Fallback to the default skin
            }
            // Get the part of the image (e.g. the head)
            BufferedImage partImage = image.getSubimage(part.getX(), part.getY(), part.getWidth(), part.getHeight());

            // Scale the image
            BufferedImage scaledImage = new BufferedImage(size, size, partImage.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(partImage, 0, 0, size, size, null);
            graphics2D.dispose();
            partImage = scaledImage;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(partImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            log.error("Failed to get {} part bytes for {}", part.name(), skin.getUrl(), ex);
            return null;
        }
    }
}
