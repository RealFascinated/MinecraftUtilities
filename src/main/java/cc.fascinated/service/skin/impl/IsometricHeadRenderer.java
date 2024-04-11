package cc.fascinated.service.skin.impl;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.model.player.Skin;
import cc.fascinated.service.skin.SkinPartRenderer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Getter @Log4j2
public class IsometricHeadRenderer extends SkinPartRenderer {

    private static final double SKEW_A = 26d / 45d;    // 0.57777777
    private static final double SKEW_B = SKEW_A * 2d; // 1.15555555

    /**
     * The head transforms
     */
    private static final AffineTransform HEAD_TRANSFORM = new AffineTransform(1d, -SKEW_A, 1, SKEW_A, 0, 0);
    private static final AffineTransform FRONT_TRANSFORM = new AffineTransform(1d, -SKEW_A, 0d, SKEW_B, 0d, SKEW_A);
    private static final AffineTransform RIGHT_TRANSFORM = new AffineTransform(1d, SKEW_A, 0d, SKEW_B, 0d, 0d);

    @Override
    public byte[] renderPart(Skin skin, String partName, boolean renderOverlay, int size) {
        double scale = (size / 8d) / 2.5;
        log.info("Getting {} part bytes for {} with size {} and scale {}", partName, skin.getUrl(), size, scale);

        double x, y, z; // The x, y, and z positions
        double zOffset = scale * 3.5d;
        double xOffset = scale * 2d;
        try {
            BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = outputImage.createGraphics();

            // Get all the required head parts
            BufferedImage headTop = ImageUtils.resize(this.getSkinPart(skin, 8, 0, 8, 8, 1), scale);
            BufferedImage headFront = ImageUtils.resize(this.getSkinPart(skin, 8, 8, 8, 8, 1), scale);
            BufferedImage headRight = ImageUtils.resize(this.getSkinPart(skin, 0, 8, 8, 8, 1), scale);

            if (renderOverlay) {
                Graphics2D headGraphics = headTop.createGraphics();
                applyOverlay(headGraphics,this.getSkinPart(skin, 40, 0, 8, 8, 1));

                headGraphics = headFront.createGraphics();
                applyOverlay(headGraphics, this.getSkinPart(skin, 16, 8, 8, 8, 1));

                headGraphics = headRight.createGraphics();
                applyOverlay(headGraphics, this.getSkinPart(skin, 32, 8, 8, 8, 1));
            }

            // Draw the head
            x = xOffset;
            y = -0.5;
            z = zOffset;
            // The head is offset by 1 pixel for whatever reason
            drawPart(graphics, headTop, HEAD_TRANSFORM,y - z, x + z, headTop.getWidth(), headTop.getHeight() + 1);

            // Draw the front of the head
            x = xOffset + 8 * scale;
            y = 0;
            z = zOffset - 0.5;
            drawPart(graphics, headFront, FRONT_TRANSFORM, y + x, x + z, headFront.getWidth(), headFront.getHeight());

            // Draw the right side of the head
            x = xOffset;
            y = 0;
            z = zOffset;
            drawPart(graphics, headRight, RIGHT_TRANSFORM, x + y + 1, z - y - 0.5, headRight.getWidth(), headRight.getHeight());

            return super.getBytes(outputImage, skin, partName);
        } catch (Exception ex) {
            log.error("Failed to get {} part bytes for {}", partName, skin.getUrl(), ex);
            throw new RuntimeException("Failed to get " + partName + " part for " + skin.getUrl());
        }
    }

    /**
     * Applies an overlay (skin layer) to the head part.
     *
     * @param graphics the graphics
     * @param part the part
     */
    private void applyOverlay(Graphics2D graphics, BufferedImage part) {
        graphics.drawImage(part, 0, 0, null);
        graphics.dispose();
    }

    /**
     * Draws a part of the head.
     *
     * @param graphics the graphics
     * @param part the part
     * @param transform the transform
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     */
    private void drawPart(Graphics2D graphics, BufferedImage part, AffineTransform transform, double x, double y, int width, int height) {
        graphics.setTransform(transform);
        graphics.drawImage(part, (int) x, (int) y, width, height, null);
    }
}