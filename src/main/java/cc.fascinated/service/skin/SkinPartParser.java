package cc.fascinated.service.skin;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.model.player.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@AllArgsConstructor @Getter
public abstract class SkinPartParser {

    /**
     * Gets the skin part image.
     *
     * @param skin the skin
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     * @param scale the scale
     * @return the skin part image
     */
    public BufferedImage getSkinPart(Skin skin, int x, int y, int width, int height, double scale) {
        try {
            BufferedImage skinImage = ImageIO.read(new ByteArrayInputStream(skin.getSkinImage()));
            BufferedImage part = skinImage.getSubimage(x, y, width, height);
            return ImageUtils.resize(part, scale);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get the skin part image.
     *
     * @param skin the skin
     * @param partName the skin part name
     * @param renderOverlay whether to render the overlay
     * @param size the output size
     * @return the skin part image
     */
    public abstract byte[] getPart(Skin skin, String partName, boolean renderOverlay, int size);
}
