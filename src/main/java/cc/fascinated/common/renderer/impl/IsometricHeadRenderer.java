package cc.fascinated.common.renderer.impl;

import cc.fascinated.common.renderer.IsometricSkinRenderer;
import cc.fascinated.model.skin.ISkinPart;
import cc.fascinated.model.skin.Skin;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class IsometricHeadRenderer extends IsometricSkinRenderer<ISkinPart.Custom> {
    public static final IsometricHeadRenderer INSTANCE = new IsometricHeadRenderer();

    private static final double SKEW_A = 26D / 45D;   // 0.57777777
    private static final double SKEW_B = SKEW_A * 2D; // 1.15555555

    private static final AffineTransform HEAD_TOP_TRANSFORM = new AffineTransform(1D, -SKEW_A, 1, SKEW_A, 0, 0);
    private static final AffineTransform FACE_TRANSFORM = new AffineTransform(1D, -SKEW_A, 0D, SKEW_B, 0d, SKEW_A);
    private static final AffineTransform HEAD_LEFT_TRANSFORM = new AffineTransform(1D, SKEW_A, 0D, SKEW_B, 0D, 0D);

    @Override
    public BufferedImage render(Skin skin, ISkinPart.Custom part, boolean renderOverlays, int size) {
        double scale = (size / 8D) / 2.5;
        double zOffset = scale * 3.5D;
        double xOffset = scale * 2D;

        BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB); // The texture to return
        Graphics2D graphics = texture.createGraphics(); // Create the graphics for drawing

        // Get the Vanilla skin parts to draw
        BufferedImage headTop = getVanillaSkinPart(skin, ISkinPart.Vanilla.HEAD_TOP, scale, renderOverlays);
        BufferedImage face = getVanillaSkinPart(skin, ISkinPart.Vanilla.FACE, scale, renderOverlays);
        BufferedImage headLeft = getVanillaSkinPart(skin, ISkinPart.Vanilla.HEAD_LEFT, scale, renderOverlays);

        // Draw the top head part
        drawPart(graphics, headTop, HEAD_TOP_TRANSFORM, -0.5 - zOffset, xOffset + zOffset, headTop.getWidth(), headTop.getHeight() + 2);

        // Draw the face part
        double x = xOffset + 8 * scale;
        drawPart(graphics, face, FACE_TRANSFORM, x, x + zOffset - 0.5, face.getWidth(), face.getHeight());

        // Draw the left head part
        drawPart(graphics, headLeft, HEAD_LEFT_TRANSFORM, xOffset + 1, zOffset - 0.5, headLeft.getWidth(), headLeft.getHeight());

        graphics.dispose();
        return texture;
    }
}
