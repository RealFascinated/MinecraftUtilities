package cc.fascinated.model.skin;

import cc.fascinated.common.renderer.SkinRenderer;
import cc.fascinated.common.renderer.impl.BodyRenderer;
import cc.fascinated.common.renderer.impl.IsometricHeadRenderer;
import cc.fascinated.common.renderer.impl.SquareRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;

public interface ISkinPart {
    Enum<?>[][] TYPES = { Vanilla.values(), Custom.values() };

    /**
     * The name of the part.
     *
     * @return the part name
     */
    String name();

    /**
     * Should this part be hidden from the
     * player skin part urls list?
     *
     * @return whether this part should be hidden
     */
    boolean hidden();

    /**
     * Renders the skin part for the skin.
     *
     * @param skin the skin
     * @param renderOverlays should the overlays be rendered
     * @param size the size of the part
     * @return the rendered skin part
     */
    BufferedImage render(Skin skin, boolean renderOverlays, int size);

    /**
     * Get a skin part by the given name.
     *
     * @param name the name of the part
     * @return the part, null if none
     */
    static ISkinPart getByName(String name) {
        name = name.toUpperCase();
        for (Enum<?>[] type : TYPES) {
            for (Enum<?> part : type) {
                if (!part.name().equals(name)) {
                    continue;
                }
                return (ISkinPart) part;
            }
        }
        return null;
    }

    @Getter
    enum Vanilla implements ISkinPart {
        // Overlays
        HEAD_OVERLAY_FACE(true, new Coordinates(40, 8), 8, 8),

        // Head
        HEAD_TOP(true, new Coordinates(8, 0), 8, 8),
        FACE(false, new Coordinates(8, 8), 8, 8, HEAD_OVERLAY_FACE),
        HEAD_LEFT(true, new Coordinates(0, 8), 8, 8),
        HEAD_RIGHT(true, new Coordinates(16, 8), 8, 8),
        HEAD_BOTTOM(true, new Coordinates(16, 0), 8, 8),
        HEAD_BACK(true, new Coordinates(24, 8), 8, 8),

        // Body
        BODY_FRONT(true, new Coordinates(20, 20), 8, 12),

        // Arms
        LEFT_ARM_TOP(true, new Coordinates(36, 48), 4, 4),
        RIGHT_ARM_TOP(true, new Coordinates(44, 16), 4, 4),

        LEFT_ARM_FRONT(true, new Coordinates(44, 20), 4, 12),
        RIGHT_ARM_FRONT(true, new Coordinates(36, 52), new LegacyCoordinates(44, 20, true), 4, 12),

        // Legs
        LEFT_LEG_FRONT(true, new Coordinates(4, 20), 4, 12), // Front
        RIGHT_LEG_FRONT(true, new Coordinates(20, 52), new LegacyCoordinates(4, 20, true), 4, 12); // Front

        /**
         * Should this part be hidden from the
         * player skin part urls list?
         */
        private final boolean hidden;

        /**
         * The coordinates of the part.
         */
        private final Coordinates coordinates;

        /**
         * The legacy coordinates of the part.
         */
        private final LegacyCoordinates legacyCoordinates;

        /**
         * The width and height of the part.
         */
        private final int width, height;

        /**
         * The overlays of the part.
         */
        private final Vanilla[] overlays;

        Vanilla(boolean hidden, Coordinates coordinates, int width, int height, Vanilla... overlays) {
            this(hidden, coordinates, null, width, height, overlays);
        }

        Vanilla(boolean hidden, Coordinates coordinates, LegacyCoordinates legacyCoordinates, int width, int height, Vanilla... overlays) {
            this.hidden = hidden;
            this.coordinates = coordinates;
            this.legacyCoordinates = legacyCoordinates;
            this.width = width;
            this.height = height;
            this.overlays = overlays;
        }

        @Override
        public boolean hidden() {
            return this.isHidden();
        }

        @Override
        public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
            return SquareRenderer.INSTANCE.render(skin, this, renderOverlays, size);
        }

        /**
         * Is this part a front arm?
         *
         * @return whether this part is a front arm
         */
        public boolean isFrontArm() {
            return this == LEFT_ARM_FRONT || this == RIGHT_ARM_FRONT;
        }

        /**
         * Does this part have legacy coordinates?
         *
         * @return whether this part has legacy coordinates
         */
        public boolean hasLegacyCoordinates() {
            return legacyCoordinates != null;
        }

        @AllArgsConstructor @Getter
        public static class Coordinates {
            /**
             * The X and Y position of the part.
             */
            private final int x, y;
        }

        @Getter
        public static class LegacyCoordinates extends Coordinates {
            /**
             * Should the part be flipped horizontally?
             */
            private final boolean flipped;

            public LegacyCoordinates(int x, int y) {
                this(x, y, false);
            }

            public LegacyCoordinates(int x, int y, boolean flipped) {
                super(x, y);
                this.flipped = flipped;
            }
        }
    }

    @AllArgsConstructor @Getter
    enum Custom implements ISkinPart {
        HEAD(IsometricHeadRenderer.INSTANCE),
        BODY(BodyRenderer.INSTANCE);

        /**
         * The renderer to use for this part
         */
        private final SkinRenderer<Custom> renderer;

        @Override
        public boolean hidden() {
            return false;
        }

        @Override
        public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
            return renderer.render(skin, this, renderOverlays, size);
        }
    }
}
