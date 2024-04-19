package xyz.mcutils.backend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.mcutils.backend.model.cache.CachedMinecraftServer;
import xyz.mcutils.backend.service.MojangService;
import xyz.mcutils.backend.service.ServerService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public ResponseEntity<CachedMinecraftServer> getServer(
            @Parameter(description = "The platform of the server", example = "java") @PathVariable String platform,
            @Parameter(description = "The hostname and port of the server", example = "aetheria.cc") @PathVariable String hostname) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                .body(serverService.getServer(platform, hostname));
    }

    @ResponseBody
    @GetMapping(value = "/icon/{hostname}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getServerIcon(
            @Parameter(description = "The hostname and port of the server", example = "aetheria.cc") @PathVariable String hostname,
            @Parameter(description = "Whether to download the image") @RequestParam(required = false, defaultValue = "false") boolean download) {
        String dispositionHeader = download ? "attachment; filename=%s.png" : "inline; filename=%s.png";

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, dispositionHeader.formatted(hostname))
                .body(serverService.getServerFavicon(hostname));
    }

    @ResponseBody
    @GetMapping(value = "/blocked/{hostname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServerBlockedStatus(
            @Parameter(description = "The hostname of the server", example = "aetheria.cc") @PathVariable String hostname) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(Map.of(
                        "blocked", mojangService.isServerBlocked(hostname)
                ));
    }
}
