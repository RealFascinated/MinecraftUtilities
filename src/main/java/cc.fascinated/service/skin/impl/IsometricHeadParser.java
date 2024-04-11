package cc.fascinated.service.skin.impl;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.model.player.Skin;
import cc.fascinated.service.skin.SkinPartParser;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Getter @Log4j2
public class IsometricHeadParser extends SkinPartParser {

    private static final double SKEW_A = 26d / 45d;    // 0.57777777
    private static final double SKEW_B = SKEW_A * 2d; // 1.15555555

    @Override
    public byte[] getPart(Skin skin, String partName, boolean renderOverlay, int size) {
        double scale = (size / 8d) / 2.5;
        log.info("Getting {} part bytes for {} with size {} and scale {}", partName, skin.getUrl(), size, scale);

        try {
            final BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

            // Get all the required head parts
            final BufferedImage headTop = ImageUtils.resize(this.getSkinPart(skin, 8, 0, 8, 8, 1), scale);
            final BufferedImage headFront = ImageUtils.resize(this.getSkinPart(skin, 8, 8, 8, 8, 1), scale);
            final BufferedImage headRight = ImageUtils.resize(this.getSkinPart(skin, 0, 8, 8, 8, 1), scale);

            if (renderOverlay) {
                // Draw the overlay on top of the gathered skin parts

                // Top overlay
                Graphics2D g = headTop.createGraphics();
                g.drawImage(this.getSkinPart(skin, 40, 0, 8, 8, 1), 0, 0, null);
                g.dispose();

                // Front overlay
                g = headFront.createGraphics();
                g.drawImage(this.getSkinPart(skin, 16, 8, 8, 8, 1), 0, 0, null);
                g.dispose();

                // Right side overlay
                g = headRight.createGraphics();
                g.drawImage(this.getSkinPart(skin, 32, 8, 8, 8, 1), 0, 0, null);
                g.dispose();
            }

            // Declare pos
            double x;
            double y;
            double z;

            // Declare offsets
            final double z_offset = scale * 3.5d;
            final double x_offset = scale * 2d;

            // Create graphics
            final Graphics2D outGraphics = outputImage.createGraphics();

            // head top
            x = x_offset;
            y = -0.5;
            z = z_offset;
            outGraphics.setTransform(new AffineTransform(1d, -SKEW_A, 1, SKEW_A, 0, 0));
            outGraphics.drawImage(headTop, (int) (y - z), (int) (x + z), headTop.getWidth(), headTop.getHeight() + 1, null);

            // head front
            x = x_offset + 8 * scale;
            y = 0;
            z = z_offset - 0.5;
            outGraphics.setTransform(new AffineTransform(1d, -SKEW_A, 0d, SKEW_B, 0d, SKEW_A));
            outGraphics.drawImage(headFront, (int) (y + x), (int) (x + z), headFront.getWidth(), headFront.getHeight(), null);

            // head right
            x = x_offset;
            y = 0;
            z = z_offset;
            outGraphics.setTransform(new AffineTransform(1d, SKEW_A, 0d, SKEW_B, 0d, 0d));
            outGraphics.drawImage(headRight, (int) (x + y + 1), (int) (z - y - 0.5), headRight.getWidth(), headRight.getHeight() + 1, null);

            // Cleanup and return
            outGraphics.dispose();

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(outputImage, "png", outputStream);
                // Cleanup
                outputStream.flush();
                outGraphics.dispose();
                log.info("Successfully got {} part bytes for {}", partName, skin.getUrl());
                return outputStream.toByteArray();
            }
        } catch (Exception ex) {
            log.error("Failed to get {} part bytes for {}", partName, skin.getUrl(), ex);
            return null;
        }
    }
}
