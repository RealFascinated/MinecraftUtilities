package cc.fascinated.api.controller;

import cc.fascinated.player.PlayerManagerService;
import cc.fascinated.player.impl.Player;
import cc.fascinated.player.impl.Skin;
import cc.fascinated.player.impl.SkinPart;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/")
public class PlayerController {

    @NonNull private final SkinPart defaultHead = Objects.requireNonNull(Skin.getDefaultHead(), "Default head is null");
    private final PlayerManagerService playerManagerService;

    @Autowired
    public PlayerController(PlayerManagerService playerManagerService) {
        this.playerManagerService = playerManagerService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE) @ResponseBody
    public ResponseEntity<?> getPlayer(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        if (player == null) {
            return new ResponseEntity<>(Map.of("error", "Player not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(player);
    }

    @GetMapping(value = "/avatar/{id}")
    public ResponseEntity<byte[]> getPlayerHead(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        byte[] headBytes;
        if (player == null) {
            headBytes = defaultHead.getPartData();
        } else {
            Skin skin = player.getSkin();
            SkinPart head = skin.getHead();
            headBytes = head.getPartData();
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .contentType(MediaType.IMAGE_PNG)
                .body(headBytes);
    }
}
