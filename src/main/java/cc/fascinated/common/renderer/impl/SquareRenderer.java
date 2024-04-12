package cc.fascinated.common.renderer.impl;

import cc.fascinated.common.renderer.SkinRenderer;
import cc.fascinated.model.skin.ISkinPart;
import cc.fascinated.model.skin.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor @Getter @Log4j2
public class SquareRenderer extends SkinRenderer<ISkinPart.Vanilla> {
    public static final SquareRenderer INSTANCE = new SquareRenderer();

    @Override
    public BufferedImage render(Skin skin, ISkinPart.Vanilla part, boolean renderOverlays, int size) {
        double scale = size / 8D;
        BufferedImage partImage = getVanillaSkinPart(skin, part, scale); // Get the part image
        if (!renderOverlays) { // Not rendering overlays
            return partImage;
        }
        // Create a new image, draw our skin part texture, and then apply overlays
        BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB); // The texture to return
        Graphics2D graphics = texture.createGraphics(); // Create the graphics for drawing
        graphics.drawImage(partImage, 0, 0, null);

        // Draw part overlays
        ISkinPart.Vanilla[] overlayParts = part.getOverlays();
        if (overlayParts != null) {
            for (ISkinPart.Vanilla overlay : overlayParts) {
                applyOverlay(graphics, getVanillaSkinPart(skin, overlay, scale));
            }
        }
        graphics.dispose();
        return texture;
    }
}
