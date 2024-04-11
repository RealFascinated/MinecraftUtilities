package cc.fascinated.service.skin.impl;

import cc.fascinated.common.ImageUtils;
import cc.fascinated.model.player.Skin;
import cc.fascinated.service.skin.SkinRenderer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter @Log4j2
public class BodyRenderer extends SkinRenderer {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 32;

    @Override
    public byte[] renderPart(Skin skin, String partName, boolean renderOverlay, int size) {
        log.info("Getting {} part bytes for {} with size {}", partName, skin.getUrl(), size);

        BufferedImage outputImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = outputImage.createGraphics();

        // Get all the required body parts
        BufferedImage head = this.getSkinPart(skin, Skin.PartPosition.HEAD, 1);
        BufferedImage body = this.getSkinPart(skin, Skin.PartPosition.BODY, 1);
        BufferedImage rightArm = this.getSkinPart(skin, Skin.PartPosition.RIGHT_ARM, 1);
        BufferedImage leftArm = this.getSkinPart(skin, Skin.PartPosition.LEFT_ARM, 1);
        BufferedImage rightLeg = this.getSkinPart(skin, Skin.PartPosition.RIGHT_LEG, 1);
        BufferedImage leftLeg = this.getSkinPart(skin, Skin.PartPosition.LEFT_LEG, 1);

        if (renderOverlay) { // Render the skin layers
            applyOverlay(head, this.getSkinPart(skin, Skin.PartPosition.HEAD_OVERLAY_FRONT, 1));
            applyOverlay(body, this.getSkinPart(skin, Skin.PartPosition.BODY_OVERLAY_FRONT, 1));
            applyOverlay(rightArm, this.getSkinPart(skin, Skin.PartPosition.RIGHT_ARM_OVERLAY, 1));
            applyOverlay(leftArm, this.getSkinPart(skin, Skin.PartPosition.LEFT_ARM_OVERLAY, 1));
            applyOverlay(rightLeg, this.getSkinPart(skin, Skin.PartPosition.RIGHT_LEG_OVERLAY, 1));
            applyOverlay(leftLeg, this.getSkinPart(skin, Skin.PartPosition.LEFT_LEG_OVERLAY, 1));
        }

        // Draw the body
        graphics.drawImage(head, 4, 0, null);
        graphics.drawImage(body, 4, 8, null);
        graphics.drawImage(rightArm, skin.getModel() == Skin.Model.SLIM ? 1 : 0, 8, null);
        graphics.drawImage(leftArm, 12, 8, null);
        graphics.drawImage(rightLeg, 4, 20, null);
        graphics.drawImage(leftLeg, 8, 20, null);

        graphics.dispose(); // Clean up
        return super.getBytes(ImageUtils.resize(outputImage, (double) size / HEIGHT), skin, partName);
    }
}