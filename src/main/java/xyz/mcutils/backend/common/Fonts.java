package xyz.mcutils.backend.common;

import lombok.extern.log4j.Log4j2;
import xyz.mcutils.backend.Main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

@Log4j2(topic = "Fonts")
public class Fonts {

    public static final Font MINECRAFT;
    public static final Font MINECRAFT_BOLD;
    public static final Font MINECRAFT_ITALIC;

    static {
        InputStream stream = Main.class.getResourceAsStream("/fonts/minecraft-font.ttf");
        try {
            MINECRAFT = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(18f);
            MINECRAFT_BOLD = MINECRAFT.deriveFont(Font.BOLD);
            MINECRAFT_ITALIC = MINECRAFT.deriveFont(Font.ITALIC);
        } catch (FontFormatException | IOException e) {
            log.error("Failed to load Minecraft font", e);
            throw new RuntimeException("Failed to load Minecraft font", e);
        }
    }
}
