package cc.fascinated.api.controller;

import cc.fascinated.player.PlayerManagerService;
import cc.fascinated.player.impl.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    private final PlayerManagerService playerManagerService;

    @Autowired
    public PlayerController(PlayerManagerService playerManagerService) {
        this.playerManagerService = playerManagerService;
    }

    @GetMapping("/{id}") @ResponseBody
    public ResponseEntity<Player> getPlayer(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }
}
