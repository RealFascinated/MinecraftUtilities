package cc.fascinated.player.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Skin {

    /**
     * The URL of the skin
     */
    private final String url;

    /**
     * The model of the skin
     */
    private final SkinType model;
}
