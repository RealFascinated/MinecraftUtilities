package xyz.mcutils.backend.common.renderer.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.mcutils.backend.common.ImageUtils;
import xyz.mcutils.backend.common.renderer.SkinRenderer;
import xyz.mcutils.backend.model.skin.ISkinPart;
import xyz.mcutils.backend.model.skin.Skin;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor @Getter @Log4j2
public class BodyRenderer extends SkinRenderer<ISkinPart.Custom> {
    public static final BodyRenderer INSTANCE = new BodyRenderer();

    @Override
    public BufferedImage render(Skin skin, ISkinPart.Custom part, boolean renderOverlays, int size) {
        BufferedImage texture = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB); // The texture to return
        Graphics2D graphics = texture.createGraphics(); // Create the graphics for drawing

        // Get the Vanilla skin parts to draw
        BufferedImage face = getVanillaSkinPart(skin, ISkinPart.Vanilla.FACE, -1, renderOverlays);
        BufferedImage body = getVanillaSkinPart(skin, ISkinPart.Vanilla.BODY_FRONT, -1, renderOverlays);
        BufferedImage leftArm = getVanillaSkinPart(skin, ISkinPart.Vanilla.LEFT_ARM_FRONT, -1, renderOverlays);
        BufferedImage rightArm = getVanillaSkinPart(skin, ISkinPart.Vanilla.RIGHT_ARM_FRONT, -1, renderOverlays);
        BufferedImage leftLeg = getVanillaSkinPart(skin, ISkinPart.Vanilla.LEFT_LEG_FRONT, -1, renderOverlays);
        BufferedImage rightLeg = getVanillaSkinPart(skin, ISkinPart.Vanilla.RIGHT_LEG_FRONT, -1, renderOverlays);

        // Draw the body parts
        graphics.drawImage(face, 4, 0, null);
        graphics.drawImage(body, 4, 8, null);
        graphics.drawImage(leftArm, skin.getModel() == Skin.Model.SLIM ? 1 : 0, 8, null);
        graphics.drawImage(rightArm, 12, 8, null);
        graphics.drawImage(leftLeg, 8, 20, null);
        graphics.drawImage(rightLeg, 4, 20, null);

        graphics.dispose();
        return ImageUtils.resize(texture, (double) size / 32);
    }
}
