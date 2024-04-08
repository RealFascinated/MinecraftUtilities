package cc.fascinated.controller;

import cc.fascinated.model.player.Player;
import cc.fascinated.model.player.Skin;
import cc.fascinated.model.response.impl.InvalidPartResponse;
import cc.fascinated.model.response.impl.PlayerNotFoundResponse;
import cc.fascinated.service.PlayerService;
import cc.fascinated.common.PlayerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/player/")
public class PlayerController {

    private final CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic();
    private final PlayerService playerManagerService;

    @Autowired
    public PlayerController(PlayerService playerManagerService) {
        this.playerManagerService = playerManagerService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE) @ResponseBody
    public ResponseEntity<?> getPlayer(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        if (player == null) { // No player with that id was found
            return new PlayerNotFoundResponse().toResponseEntity();
        }
        // Return the player
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(player);

    }

    @GetMapping(value = "/{part}/{id}")
    public ResponseEntity<?> getPlayerHead(@PathVariable String part,
                                                @PathVariable String id,
                                                @RequestParam(required = false, defaultValue = "250") int size) {
        Player player = playerManagerService.getPlayer(id);
        byte[] partBytes = new byte[0];
        if (player != null) { // The player exists
            Skin skin = player.getSkin();
            Skin.Parts skinPart = Skin.Parts.fromName(part);
            if (skinPart == null) { // Unknown part name
                return new InvalidPartResponse().toResponseEntity();
            }
            partBytes = PlayerUtils.getSkinPartBytes(skin, skinPart, size);
        }
        if (partBytes == null) { // Fallback to the default head
            partBytes = PlayerUtils.getSkinPartBytes(Skin.DEFAULT_SKIN, Skin.Parts.HEAD, size);
        }
        // Return the part image
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .contentType(MediaType.IMAGE_PNG)
                .body(partBytes);
    }
}
