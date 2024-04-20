package xyz.mcutils.backend.common;

import xyz.mcutils.backend.Main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {

    public static final Font MINECRAFT;
    public static final Font MINECRAFT_BOLD;

    static {
        InputStream stream = Main.class.getResourceAsStream("/fonts/minecraft-font.ttf");
        try {
            MINECRAFT = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(18f);
            MINECRAFT_BOLD = MINECRAFT.deriveFont(Font.BOLD);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
