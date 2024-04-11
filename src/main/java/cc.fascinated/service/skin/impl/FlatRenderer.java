package cc.fascinated.service.skin.impl;

import cc.fascinated.model.player.Skin;
import cc.fascinated.service.skin.SkinPartRenderer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Getter @Log4j2
public class FlatRenderer extends SkinPartRenderer {

    /**
     * The x and y position of the part.
     */
    private final int x, y;

    /**
     * The width and height of the part.
     */
    private final int widthAndHeight;

    /**
     * Constructs a new {@link FlatRenderer}.
     *
     * @param x the x position of the part
     * @param y the y position of the part
     * @param widthAndHeight the width and height of the part
     */
    public FlatRenderer(int x, int y, int widthAndHeight) {
        this.x = x;
        this.y = y;
        this.widthAndHeight = widthAndHeight;
    }

    @Override
    public byte[] renderPart(Skin skin, String partName, boolean renderOverlay, int size) {
        double scale = (double) size / this.widthAndHeight;
        log.info("Getting {} part bytes for {} with size {} and scale {}", partName, skin.getUrl(), size, scale);

        try {
            BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = outputImage.createGraphics();

            graphics.setTransform(AffineTransform.getScaleInstance(scale, scale));
            graphics.drawImage(this.getSkinPart(skin, this.x, this.y, this.widthAndHeight, this.widthAndHeight, 1), 0, 0, null);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(outputImage, "png", outputStream);
                // Cleanup
                outputStream.flush();
                graphics.dispose();
                log.info("Successfully got {} part bytes for {}", partName, skin.getUrl());
                return outputStream.toByteArray();
            }
        } catch (Exception ex) {
            log.error("Failed to get {} part bytes for {}", partName, skin.getUrl(), ex);
            throw new RuntimeException("Failed to get " + partName + " part for " + skin.getUrl());
        }
    }
}
