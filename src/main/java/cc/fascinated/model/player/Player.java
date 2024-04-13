package cc.fascinated.model.player;

import cc.fascinated.common.Tuple;
import cc.fascinated.common.UUIDUtils;
import cc.fascinated.model.mojang.MojangProfile;
import cc.fascinated.model.skin.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter @AllArgsConstructor
public class Player {

    /**
     * The UUID of the player
     */
    private final UUID uniqueId;

    /**
     * The trimmed UUID of the player
     */
    private final String trimmedUniqueId;

    /**
     * The username of the player
     */
    private final String username;

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
    private MojangProfile.ProfileProperty[] rawProperties;

    public Player(MojangProfile profile) {
        this.uniqueId = UUIDUtils.addDashes(profile.getId());
        this.trimmedUniqueId = UUIDUtils.removeDashes(this.uniqueId);
        this.username = profile.getName();
        this.rawProperties = profile.getProperties();

        // Get the skin and cape
        Tuple<Skin, Cape> skinAndCape = profile.getSkinAndCape();
        if (skinAndCape != null) {
            this.skin = skinAndCape.getLeft();
            this.cape = skinAndCape.getRight();
        }
    }
}
