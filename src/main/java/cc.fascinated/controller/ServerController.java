package cc.fascinated.controller;

import cc.fascinated.common.ServerUtils;
import cc.fascinated.common.Tuple;
import cc.fascinated.model.cache.CachedMinecraftServer;
import cc.fascinated.service.MojangService;
import cc.fascinated.service.ServerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Server Controller", description = "The Server Controller is used to get information about a server.")
@RequestMapping(value = "/server/")
public class ServerController {

    private final ServerService serverService;
    private final MojangService mojangService;

    @Autowired
    public ServerController(ServerService serverService, MojangService mojangService) {
        this.serverService = serverService;
        this.mojangService = mojangService;
    }

    @ResponseBody
    @GetMapping(value = "/{platform}/{hostname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CachedMinecraftServer getServer(
            @Parameter(description = "The platform of the server", example = "java") @PathVariable String platform,
            @Parameter(description = "The hostname and port of the server", example = "play.hypixel.net") @PathVariable String hostname) {
        return serverService.getServer(platform, hostname);
    }

    @ResponseBody
    @GetMapping(value = "/icon/{hostname}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getServerIcon(
            @Parameter(description = "The hostname and port of the server", example = "play.hypixel.net") @PathVariable String hostname,
            @Parameter(description = "Whether to download the image") @RequestParam(required = false, defaultValue = "false") boolean download) {
        String dispositionHeader = download ? "attachment; filename=%s.png" : "inline; filename=%s.png";

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, dispositionHeader.formatted(hostname))
                .body(serverService.getServerFavicon(hostname));
    }

    @ResponseBody
    @GetMapping(value = "/blocked/{hostname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServerBlockedStatus(
            @Parameter(description = "The hostname of the server", example = "play.hypixel.net") @PathVariable String hostname) {
        return ResponseEntity.ok(Map.of(
                "banned", mojangService.isServerBlocked(hostname)
        ));
    }
}
