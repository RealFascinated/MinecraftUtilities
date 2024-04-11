package cc.fascinated.service.skin.impl;

import cc.fascinated.model.player.Skin;
import cc.fascinated.service.skin.SkinRenderer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Getter @Log4j2
public class HeadRenderer extends SkinRenderer {

    @Override
    public byte[] renderPart(Skin skin, String partName, boolean renderOverlay, int size) {
        double scale = (double) size / 8d;
        log.info("Getting {} part bytes for {} with size {} and scale {}", partName, skin.getUrl(), size, scale);

        BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = outputImage.createGraphics();

        graphics.setTransform(AffineTransform.getScaleInstance(scale, scale));
        graphics.drawImage(this.getSkinPart(skin, Skin.PartPosition.HEAD, 1), 0, 0, null);

        if (renderOverlay) { // Render the skin layers
            applyOverlay(outputImage, this.getSkinPart(skin, Skin.PartPosition.HEAD_OVERLAY_FRONT, 1));
        }

        return super.getBytes(outputImage, skin, partName);
    }
}
