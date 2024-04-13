package xyz.mcutils.backend.common.renderer;

import xyz.mcutils.backend.model.skin.ISkinPart;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class IsometricSkinRenderer<T extends ISkinPart> extends SkinRenderer<T> {

    /**
     * Draw a part onto the texture.
     *
     * @param graphics  the graphics to draw to
     * @param partImage the part image to draw
     * @param transform the transform to apply
     * @param x         the x position to draw at
     * @param y         the y position to draw at
     * @param width     the part image width
     * @param height    the part image height
     */
    protected final void drawPart(Graphics2D graphics, BufferedImage partImage, AffineTransform transform,
                                  double x, double y, int width, int height) {
        graphics.setTransform(transform);
        graphics.drawImage(partImage, (int) x, (int) y, width, height, null);
    }
}
