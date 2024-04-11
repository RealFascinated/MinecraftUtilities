package cc.fascinated.service.skin.impl;

import cc.fascinated.model.player.Skin;
import cc.fascinated.service.skin.SkinRenderer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Getter @Log4j2
public class IsometricHeadRenderer extends SkinRenderer {

    private static final double SKEW_A = 26d / 45d;   // 0.57777777
    private static final double SKEW_B = SKEW_A * 2d; // 1.15555555

    /**
     * The head transforms
     */
    private static final AffineTransform HEAD_TOP_TRANSFORM = new AffineTransform(1D, -SKEW_A, 1, SKEW_A, 0, 0);
    private static final AffineTransform FACE_TRANSFORM = new AffineTransform(1D, -SKEW_A, 0D, SKEW_B, 0d, SKEW_A);
    private static final AffineTransform HEAD_LEFT_TRANSFORM = new AffineTransform(1D, SKEW_A, 0D, SKEW_B, 0D, 0D);

    @Override
    public byte[] renderPart(Skin skin, String partName, boolean renderOverlay, int size) {
        double scale = (size / 8d) / 2.5;
        log.info("Getting {} part bytes for {} with size {} and scale {}", partName, skin.getUrl(), size, scale);

        double zOffset = scale * 3.5d;
        double xOffset = scale * 2d;
        BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = outputImage.createGraphics();

        // Get all the required head parts
        BufferedImage headTop = this.getSkinPart(skin, Skin.PartPosition.HEAD_TOP, scale);
        BufferedImage headFront = this.getSkinPart(skin, Skin.PartPosition.HEAD_FACE, scale);
        BufferedImage headLeft = this.getSkinPart(skin, Skin.PartPosition.HEAD_LEFT, scale);

        if (renderOverlay) { // Render the skin layers
            applyOverlay(headTop, this.getSkinPart(skin, Skin.PartPosition.HEAD_OVERLAY_TOP, scale));
            applyOverlay(headFront, this.getSkinPart(skin, Skin.PartPosition.HEAD_OVERLAY_FACE, scale));
            applyOverlay(headLeft, this.getSkinPart(skin, Skin.PartPosition.HEAD_OVERLAY_LEFT, scale));
        }

        // Draw the top of the left
        drawPart(graphics, headTop, HEAD_TOP_TRANSFORM, -0.5 - zOffset, xOffset + zOffset, headTop.getWidth(), headTop.getHeight() + 2);

        // Draw the face of the head
        double x = xOffset + 8 * scale;
        drawPart(graphics, headFront, FACE_TRANSFORM, x, x + zOffset - 0.5, headFront.getWidth(), headFront.getHeight());

        // Draw the left side of the head
        drawPart(graphics, headLeft, HEAD_LEFT_TRANSFORM, xOffset + 1, zOffset - 0.5, headLeft.getWidth(), headLeft.getHeight());

        return super.getBytes(outputImage, skin, partName);
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