package cc.fascinated.service.player.impl;

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
     * The information about the part.
     */
    private final Skin.SkinPartEnum skinPartEnum;

    /**
     * The part data from the skin.
     */
    private byte[] partBytes;

    public SkinPart(byte[] data, Skin.SkinPartEnum skinPartEnum) {
        this.data = data;
        this.skinPartEnum = skinPartEnum;
    }

    /**
     * Gets the part data from the skin.
     *
     * @return the part data
     */
    public byte[] getPartData(int size) {
        if (size == -1) {
            size = this.skinPartEnum.getDefaultSize();
        }

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(this.data));
            if (image == null) {
                return null;
            }
            // Get the part of the image (e.g. the head)
            BufferedImage partImage = image.getSubimage(this.skinPartEnum.getX(), this.skinPartEnum.getY(), this.skinPartEnum.getWidth(), this.skinPartEnum.getHeight());

            // Scale the image
            BufferedImage scaledImage = new BufferedImage(size, size, partImage.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(partImage, 0, 0, size, size, null);
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
