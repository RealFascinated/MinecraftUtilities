package cc.fascinated.model.player;

import cc.fascinated.service.mojang.model.MojangProfile;
import cc.fascinated.util.Tuple;
import cc.fascinated.util.UUIDUtils;
import lombok.Getter;

import java.util.List;
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
     * The skin of the player, null if the
     * player does not have a skin
     */
    private Skin skin;

    /**
     * The cape of the player, null if the
     * player does not have a cape
     */
    private Cape cape;

    /**
     * The raw properties of the player
     */
    private final List<MojangProfile.ProfileProperty> rawProperties;

    public Player(MojangProfile profile) {
        this.uuid = UUID.fromString(UUIDUtils.addUUIDDashes(profile.getId()));
        this.name = profile.getName();
        this.rawProperties = profile.getProperties();

        // Get the skin and cape
        Tuple<Skin, Cape> skinAndCape = profile.getSkinAndCape();
        if (skinAndCape != null) {
            this.skin = skinAndCape.getLeft();
            this.cape = skinAndCape.getRight();
        }
    }
}
