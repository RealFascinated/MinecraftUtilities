package xyz.mcutils.backend.common;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Log4j2(topic = "Image Utils")
public class ImageUtils {
    /**
     * Scale the given image to the provided scale.
     *
     * @param image the image to scale
     * @param scale  the scale to scale the image to
     * @return the scaled image
     */
    public static BufferedImage resize(BufferedImage image, double scale) {
        BufferedImage scaled = new BufferedImage((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        graphics.drawImage(image, AffineTransform.getScaleInstance(scale, scale), null);
        graphics.dispose();
        return scaled;
    }

    /**
     * Flip the given image.
     *
     * @param image the image to flip
     * @return the flipped image
     */
    public static BufferedImage flip(@NotNull final BufferedImage image) {
        BufferedImage flipped = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = flipped.createGraphics();
        graphics.drawImage(image, image.getWidth(), 0, 0, image.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
        graphics.dispose();
        return flipped;
    }

    /**
     * Convert an image to bytes.
     *
     * @param image the image to convert
     * @return the image as bytes
     */
    @SneakyThrows
    public static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new Exception("Failed to convert image to bytes", e);
        }
    }

    /**
     * Convert a base64 string to an image.
     *
     * @param base64 the base64 string to convert
     * @return the image
     */
    @SneakyThrows
    public static BufferedImage base64ToImage(String base64) {
        String favicon = base64.contains("data:image/png;base64,") ? base64.split(",")[1] : base64;

        try {
            return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(favicon)));
        } catch (Exception e) {
            throw new Exception("Failed to convert base64 to image", e);
        }
    }
}
