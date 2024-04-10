package cc.fascinated.model.player;

import cc.fascinated.common.Tuple;
import cc.fascinated.common.UUIDUtils;
import cc.fascinated.model.mojang.MojangProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter @AllArgsConstructor
public class Player {

    /**
     * The UUID of the player
     */
    @Id private final UUID uuid;

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

    public Player(MojangProfile profile) {
        this.uuid = UUIDUtils.addDashes(profile.getId());
        this.username = profile.getName();

        // Get the skin and cape
        Tuple<Skin, Cape> skinAndCape = profile.getSkinAndCape();
        if (skinAndCape != null) {
            this.skin = skinAndCape.getLeft();
            this.cape = skinAndCape.getRight();
        }
    }
}
