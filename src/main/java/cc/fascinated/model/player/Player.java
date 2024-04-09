package cc.fascinated.model.player;

import cc.fascinated.common.Tuple;
import cc.fascinated.common.UUIDUtils;
import cc.fascinated.model.mojang.MojangProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {

    /**
     * The UUID of the player
     */
    private final UUID uuid;

    /**
     * The username of the player
     */
    @JsonProperty("username")
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

    public Player(MojangProfile profile) {
        this.uuid = UUID.fromString(UUIDUtils.addUuidDashes(profile.getId()));
        this.name = profile.getName();

        // Get the skin and cape
        Tuple<Skin, Cape> skinAndCape = profile.getSkinAndCape();
        if (skinAndCape != null) {
            this.skin = skinAndCape.getLeft();
            this.cape = skinAndCape.getRight();
        }
    }
}
