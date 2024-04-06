package cc.fascinated.player.impl;

public enum SkinType {

    DEFAULT,
    SLIM;

    /**
     * Get the skin type from a string
     *
     * @param string the string
     * @return the skin type
     */
    public static SkinType fromString(String string) {
        for (SkinType type : values()) {
            if (type.name().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return null;
    }
}
