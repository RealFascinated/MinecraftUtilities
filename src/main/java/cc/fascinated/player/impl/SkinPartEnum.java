package cc.fascinated.player.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum SkinPartEnum {

    HEAD(8, 8, 8, 8, 20);

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int scale;
}
