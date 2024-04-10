package cc.fascinated.controller;

import cc.fascinated.common.PlayerUtils;
import cc.fascinated.model.player.Player;
import cc.fascinated.model.player.Skin;
import cc.fascinated.service.PlayerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@Tag(name = "Player Controller", description = "The Player Controller is used to get information about a player.")
@RequestMapping(value = "/player/")
public class PlayerController {

    private final CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic();
    private final PlayerService playerManagerService;

    @Autowired
    public PlayerController(PlayerService playerManagerService) {
        this.playerManagerService = playerManagerService;
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlayer(@PathVariable String id) {
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(playerManagerService.getPlayer(id));
    }

    @GetMapping(value = "/{part}/{id}")
    public ResponseEntity<?> getPlayerHead(@PathVariable String part,
                                                @PathVariable String id,
                                                @RequestParam(required = false, defaultValue = "256") int size) {
        Player player = playerManagerService.getPlayer(id);
        Skin.Parts skinPart = Skin.Parts.fromName(part);

        // Return the part image
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=%s.png".formatted(player.getUsername()))
                .body(PlayerUtils.getSkinPartBytes(player.getSkin(), skinPart, size));
    }
}
