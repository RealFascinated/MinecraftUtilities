package xyz.mcutils.backend.common.renderer.impl.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.mcutils.backend.common.renderer.SkinRenderer;
import xyz.mcutils.backend.model.skin.ISkinPart;
import xyz.mcutils.backend.model.skin.Skin;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor @Getter @Log4j2(topic = "Skin Renderer/Square")
public class SquareRenderer extends SkinRenderer<ISkinPart.Vanilla> {
    public static final SquareRenderer INSTANCE = new SquareRenderer();

    @Override
    public BufferedImage render(Skin skin, ISkinPart.Vanilla part, boolean renderOverlays, int size) {
        double scale = size / 8D;
        BufferedImage partImage = getVanillaSkinPart(skin, part, scale, renderOverlays); // Get the part image
        if (!renderOverlays) { // Not rendering overlays
            return partImage;
        }
        // Create a new image, draw our skin part texture, and then apply overlays
        BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB); // The texture to return
        Graphics2D graphics = texture.createGraphics(); // Create the graphics for drawing
        graphics.drawImage(partImage, 0, 0, null);

        graphics.dispose();
        return texture;
    }
}
