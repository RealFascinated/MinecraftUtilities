package xyz.mcutils.backend.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.mcutils.backend.common.Tuple;
import xyz.mcutils.backend.common.UUIDUtils;
import xyz.mcutils.backend.model.skin.Skin;
import xyz.mcutils.backend.model.token.MojangProfileToken;

import java.util.UUID;

@Getter @AllArgsConstructor @NoArgsConstructor
public class Player {

    /**
     * The UUID of the player
     */
    private UUID uniqueId;

    /**
     * The trimmed UUID of the player
     */
    private String trimmedUniqueId;

    /**
     * The username of the player
     */
    private String username;

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
    private MojangProfileToken.ProfileProperty[] rawProperties;

    public Player(MojangProfileToken profile) {
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
