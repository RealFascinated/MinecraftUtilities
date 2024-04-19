package xyz.mcutils.backend.common.renderer;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.mcutils.backend.common.ImageUtils;
import xyz.mcutils.backend.model.skin.ISkinPart;
import xyz.mcutils.backend.model.skin.Skin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Log4j2(topic = "Skin Renderer")
public abstract class SkinRenderer<T extends ISkinPart> {

    /**
     * Get the texture of a part of the skin.
     *
     * @param skin the skin to get the part texture from
     * @param part the part of the skin to get
     * @param size the size to scale the texture to
     * @param renderOverlays should the overlays be rendered
     * @return the texture of the skin part
     */
    @SneakyThrows
    public BufferedImage getVanillaSkinPart(Skin skin, ISkinPart.Vanilla part, double size, boolean renderOverlays) {
        ISkinPart.Vanilla.Coordinates coordinates = part.getCoordinates(); // The coordinates of the part

        // The skin texture is legacy, use legacy coordinates
        if (skin.isLegacy() && part.hasLegacyCoordinates()) {
            coordinates = part.getLegacyCoordinates();
        }
        int width = part.getWidth(); // The width of the part
        if (skin.getModel() == Skin.Model.SLIM && part.isFrontArm()) {
            width--;
        }
        BufferedImage skinImage = ImageIO.read(new ByteArrayInputStream(skin.getSkinImage())); // The skin texture
        BufferedImage partTexture = getSkinPartTexture(skinImage, coordinates.getX(), coordinates.getY(), width, part.getHeight(), size);
        if (coordinates instanceof ISkinPart.Vanilla.LegacyCoordinates legacyCoordinates && legacyCoordinates.isFlipped()) {
            partTexture = ImageUtils.flip(partTexture);
        }

        // Draw part overlays
        ISkinPart.Vanilla[] overlayParts = part.getOverlays();
        if (overlayParts != null && renderOverlays) {
            log.info("Applying overlays to part: {}", part.name());
            for (ISkinPart.Vanilla overlay : overlayParts) {
                applyOverlay(partTexture.createGraphics(), getVanillaSkinPart(skin, overlay, size, false));
            }
        }

        return partTexture;
    }

    /**
     * Get the texture of a specific part of the skin.
     *
     * @param skinImage the skin image to get the part from
     * @param x         the x position of the part
     * @param y         the y position of the part
     * @param width     the width of the part
     * @param height    the height of the part
     * @param size      the size to scale the part to
     * @return the texture of the skin part
     */
    @SneakyThrows
    private BufferedImage getSkinPartTexture(BufferedImage skinImage, int x, int y, int width, int height, double size) {
        // Create a new BufferedImage for the part of the skin texture
        BufferedImage headTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Crop just the part we want based on our x, y, width, and height
        headTexture.getGraphics().drawImage(skinImage, 0, 0, width, height, x, y, x + width, y + height, null);

        // Scale the skin part texture
        if (size > 0D) {
            headTexture = ImageUtils.resize(headTexture, size);
        }
        return headTexture;
    }

    /**
     * Apply an overlay to a texture.
     *
     * @param graphics     the graphics to overlay on
     * @param overlayImage the part to overlay
     */
    protected void applyOverlay(Graphics2D graphics, BufferedImage overlayImage) {
        try {
            graphics.drawImage(overlayImage, 0, 0, null);
            graphics.dispose();
        } catch (Exception ignored) {
            // We can safely ignore this, legacy
            // skins don't have overlays
        }
    }

    /**
     * Renders the skin part for the player's skin.
     *
     * @param skin the player's skin
     * @param part the skin part to render
     * @param renderOverlays should the overlays be rendered
     * @param size the size of the part
     * @return the rendered skin part
     */
    public abstract BufferedImage render(Skin skin, T part, boolean renderOverlays, int size);
}
