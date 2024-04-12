package cc.fascinated.controller;

import cc.fascinated.model.cache.CachedPlayer;
import cc.fascinated.model.cache.CachedPlayerName;
import cc.fascinated.service.PlayerService;
import io.swagger.v3.oas.annotations.Parameter;
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
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerManagerService) {
        this.playerService = playerManagerService;
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlayer(
            @Parameter(description = "The UUID or Username of the player", example = "ImFascinated") @PathVariable String id) {
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(playerService.getPlayer(id));
    }

    @ResponseBody
    @GetMapping(value = "/uuid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CachedPlayerName getPlayerUuid(
            @Parameter(description = "The UUID or Username of the player", example = "ImFascinated") @PathVariable String id) {
        return playerService.usernameToUuid(id);
    }

    @GetMapping(value = "/{part}/{id}")
    public ResponseEntity<?> getPlayerHead(
            @Parameter(description = "The part of the skin", example = "head") @PathVariable String part,
            @Parameter(description = "The UUID or Username of the player", example = "ImFascinated") @PathVariable String id,
            @Parameter(description = "The size of the image", example = "256") @RequestParam(required = false, defaultValue = "256") int size,
            @Parameter(description = "Whether to render the skin overlay (skin layers)", example = "false") @RequestParam(required = false, defaultValue = "false") boolean overlay,
            @Parameter(description = "Whether to download the image") @RequestParam(required = false, defaultValue = "false") boolean download) {
        CachedPlayer player = playerService.getPlayer(id);
        String dispositionHeader = download ? "attachment; filename=%s.png" : "inline; filename=%s.png";

        // Return the part image
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, dispositionHeader.formatted(player.getUsername()))
                .body(playerService.getSkinPart(player, part, overlay, size).getBytes());
    }
}
