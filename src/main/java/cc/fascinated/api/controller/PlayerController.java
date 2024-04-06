package cc.fascinated.api.controller;

import cc.fascinated.player.PlayerManagerService;
import cc.fascinated.player.impl.Player;
import cc.fascinated.player.impl.Skin;
import cc.fascinated.player.impl.SkinPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class PlayerController {

    private final PlayerManagerService playerManagerService;

    @Autowired
    public PlayerController(PlayerManagerService playerManagerService) {
        this.playerManagerService = playerManagerService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE) @ResponseBody
    public ResponseEntity<Player> getPlayer(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @GetMapping(value = "/avatar/{id}")
    public ResponseEntity<byte[]> getPlayerHead(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        if (player == null) {
            return null;
        }
        Skin skin = player.getSkin();
        SkinPart head = skin.getHead();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(head.getPartData());
    }
}
