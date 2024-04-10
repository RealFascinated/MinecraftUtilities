package cc.fascinated.controller;

import cc.fascinated.common.ServerUtils;
import cc.fascinated.common.Tuple;
import cc.fascinated.model.cache.CachedMinecraftServer;
import cc.fascinated.service.ServerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Server Controller", description = "The Server Controller is used to get information about a server.")
@RequestMapping(value = "/server/")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @ResponseBody
    @GetMapping(value = "/{platform}/{hostnameAndPort}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CachedMinecraftServer getServer(
            @Parameter(description = "The platform of the server", example = "java")
            @PathVariable String platform,
            @Parameter(description = "The hostname and port of the server", example = "play.hypixel.net")
            @PathVariable String hostnameAndPort) {
        Tuple<String, Integer> host = ServerUtils.getHostnameAndPort(hostnameAndPort);
        return serverService.getServer(platform, host.getLeft(), host.getRight());
    }

    @ResponseBody
    @GetMapping(value = "/icon/{hostnameAndPort}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServerIcon(
            @Parameter(description = "The hostname and port of the server", example = "play.hypixel.net")
            @PathVariable String hostnameAndPort) {
        Tuple<String, Integer> host = ServerUtils.getHostnameAndPort(hostnameAndPort);
        String hostname = host.getLeft();
        int port = host.getRight();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=%s.png".formatted(ServerUtils.getAddress(hostname, port)))
                .body(serverService.getServerFavicon(hostname, port));
    }
}
