package cc.fascinated.common;

import jakarta.validation.constraints.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageUtils {

    /**
     * Resize an image.
     *
     * @param src the source image
     * @param scale the scale factor
     * @return the scaled image
     */
    public static BufferedImage resize(@NotNull final BufferedImage src, final double scale) {
        BufferedImage scaled = new BufferedImage((int) (src.getWidth() * scale), (int) (src.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        graphics.drawImage(src, AffineTransform.getScaleInstance(scale, scale), null);
        graphics.dispose();
        return scaled;
    }
}
