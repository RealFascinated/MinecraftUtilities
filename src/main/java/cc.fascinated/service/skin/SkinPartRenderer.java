package cc.fascinated.service.skin;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.model.player.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@AllArgsConstructor @Getter @Log4j2
public abstract class SkinPartRenderer {

    /**
     * Gets the skin part image.
     *
     * @param skin the skin
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     * @param scale the scale
     * @return the skin part image
     */
    public BufferedImage getSkinPart(Skin skin, int x, int y, int width, int height, double scale) {
        try {
            BufferedImage skinImage = ImageIO.read(new ByteArrayInputStream(skin.getSkinImage()));
            BufferedImage part = skinImage.getSubimage(x, y, width, height);
            return ImageUtils.resize(part, scale);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Gets the bytes of an image.
     *
     * @param image the image
     * @param skin the skin
     * @param partName the part name
     * @return the bytes
     */
    public byte[] getBytes(BufferedImage image, Skin skin, String partName) throws BadRequestException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            // Cleanup
            outputStream.flush();
            log.info("Successfully got {} part bytes for {}", partName, skin.getUrl());
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new BadRequestException("Failed to get " + partName + " part bytes for " + skin.getUrl());
        }
    }

    /**
     * Renders a skin part.
     *
     * @param skin the skin
     * @param partName the skin part name
     * @param renderOverlay whether to render the overlay
     * @param size the output size
     * @return the skin part image
     */
    public abstract byte[] renderPart(Skin skin, String partName, boolean renderOverlay, int size) throws BadRequestException;
}
