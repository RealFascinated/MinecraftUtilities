package cc.fascinated.service.player.impl;

import cc.fascinated.service.mojang.types.MojangProfile;
import cc.fascinated.util.Tuple;
import cc.fascinated.util.UUIDUtils;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {

    /**
     * The UUID of the player
     */
    private final UUID uuid;

    /**
     * The name of the player
     */
    private final String name;

    /**
     * The skin of the player
     * <p>
     *     This will be null if the player does not have a skin.
     * </p>
     */
    private Skin skin;

    /**
     * The cape of the player
     * <p>
     *     This will be null if the player does not have a cape.
     * </p>
     */
    private Cape cape;

    public Player(MojangProfile profile) {
        this.uuid = UUID.fromString(UUIDUtils.addUUIDDashes(profile.getId()));
        this.name = profile.getName();

        // Get the skin and cape
        Tuple<Skin, Cape> skinAndCape = profile.getSkinAndCape();
        if (skinAndCape != null) {
            this.skin = skinAndCape.getLeft();
            this.cape = skinAndCape.getRight();
        }
    }
}
