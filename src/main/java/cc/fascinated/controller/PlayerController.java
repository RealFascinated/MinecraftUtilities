package cc.fascinated.controller;

import cc.fascinated.service.PlayerService;
import cc.fascinated.model.player.Player;
import cc.fascinated.model.player.Skin;
import cc.fascinated.model.player.SkinPart;
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
@RequestMapping(value = "/player/")
public class PlayerController {

    private final CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic();
    @NonNull
    private final SkinPart defaultHead = Objects.requireNonNull(Skin.getDefaultHead(), "Default head is null");

    private final PlayerService playerManagerService;

    @Autowired
    public PlayerController(PlayerService playerManagerService) {
        this.playerManagerService = playerManagerService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE) @ResponseBody
    public ResponseEntity<?> getPlayer(@PathVariable String id) {
        Player player = playerManagerService.getPlayer(id);
        if (player == null) {
            return new ResponseEntity<>(Map.of("error", "Player not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(player);

    }

    @GetMapping(value = "/{part}/{id}")
    public ResponseEntity<byte[]> getPlayerHead(@PathVariable String part,
                                                @PathVariable String id,
                                                @RequestParam(required = false, defaultValue = "250") int size) {
        Player player = playerManagerService.getPlayer(id);
        byte[] headBytes = new byte[0];
        if (player != null) { // The player exists
            Skin skin = player.getSkin();
            SkinPart skinPart = skin.getPart(part);
            if (skinPart != null) {
                headBytes = skinPart.getPartData(size);
            }
        }
        
        if (headBytes == null) { // Fallback to the default head
            headBytes = defaultHead.getPartData(size);
        }
        
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .contentType(MediaType.IMAGE_PNG)
                .body(headBytes);
    }
}
