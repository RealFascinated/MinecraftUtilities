package cc.fascinated.player.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Getter @Log4j2
public class SkinPart {

    /**
     * The whole skin data.
     */
    private final byte[] data;

    /**
     * The X coordinate of the part.
     */
    private final int x;

    /**
     * The Y coordinate of the part.
     */
    private final int y;

    /**
     * The width of the part.
     */
    private final int width;

    /**
     * The height of the part.
     */
    private final int height;

    /**
     * The scale of the part output.
     */
    private final int scale;

    /**
     * The part data from the skin.
     */
    private byte[] partBytes;

    public SkinPart(byte[] data, int x, int y, int width, int height, int scale) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    /**
     * Gets the part data from the skin.
     *
     * @return the part data
     */
    public byte[] getPartData() {
        if (this.partBytes != null) {
            return this.partBytes;
        }

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(this.data));
            if (image == null) {
                return null;
            }
            BufferedImage partImage = image.getSubimage(this.x, this.y, this.width, this.height);

            // Scale the image
            int width = partImage.getWidth() * this.scale;
            int height = partImage.getHeight() * this.scale;
            BufferedImage scaledImage = new BufferedImage(width, height, partImage.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(partImage, 0, 0, width, height, null);
            graphics2D.dispose();
            partImage = scaledImage;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(partImage, "png", byteArrayOutputStream);
            this.partBytes = byteArrayOutputStream.toByteArray();
            return this.partBytes;
        } catch (Exception ex) {
            log.error("Failed to read image from skin data.", ex);
            return null;
        }
    }
}
