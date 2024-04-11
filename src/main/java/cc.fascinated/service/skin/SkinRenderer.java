package cc.fascinated.service.skin;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.model.player.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@AllArgsConstructor @Getter @Log4j2
public abstract class SkinRenderer {

    /**
     * Gets the skin image.
     *
     * @param skin the skin
     * @return the skin image
     */
    public BufferedImage getSkinImage(Skin skin) {
        try {
            return ImageIO.read(new ByteArrayInputStream(skin.getSkinImage()));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Gets the skin part image.
     *
     * @param skin the skin
     * @param position the part position information
     * @param scale the scale
     * @return the skin part image
     */
    public BufferedImage getSkinPart(Skin skin, Skin.PartPosition position, double scale) {
        try {
            BufferedImage skinImage = this.getSkinImage(skin);
            if (skinImage == null) {
                return null;
            }
            BufferedImage part;
            Skin.LegacyPartPositionData legacyData = position.getLegacyData();
            if (skin.isLegacy() && legacyData != null) {
                part = skinImage.getSubimage(legacyData.getX(), legacyData.getY(), position.getWidth(), position.getHeight());
                if (legacyData.isFlipped()) {
                    part = ImageUtils.flip(part);
                }
            } else {
                part = skinImage.getSubimage(position.getX(), position.getY(), position.getWidth(), position.getHeight());
            }
            if (part == null) {
                return null;
            }
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
     * Applies an overlay (skin layer) to the head part.
     *
     * @param graphics the graphics
     * @param part the part
     */
    public void applyOverlay(Graphics2D graphics, BufferedImage part) {
        if (part == null) {
            return;
        }
        graphics.drawImage(part, 0, 0, null);
        graphics.dispose();
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
