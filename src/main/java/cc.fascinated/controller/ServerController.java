package cc.fascinated.controller;

import cc.fascinated.common.ServerUtils;
import cc.fascinated.common.Tuple;
import cc.fascinated.model.cache.CachedMinecraftServer;
import cc.fascinated.model.server.JavaMinecraftServer;
import cc.fascinated.model.server.MinecraftServer;
import cc.fascinated.service.ServerService;
import cc.fascinated.service.pinger.impl.JavaMinecraftServerPinger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/server/")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @ResponseBody
    @GetMapping(value = "/{platform}/{hostnameAndPort}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CachedMinecraftServer getServer(@PathVariable String platform, @PathVariable String hostnameAndPort) {
        Tuple<String, Integer> host = ServerUtils.getHostnameAndPort(hostnameAndPort);
        return serverService.getServer(platform, host.getLeft(), host.getRight());
    }

    @ResponseBody
    @GetMapping(value = "/icon/{hostnameAndPort}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServerIcon(@PathVariable String hostnameAndPort) {
        Tuple<String, Integer> host = ServerUtils.getHostnameAndPort(hostnameAndPort);
        String hostname = host.getLeft();
        int port = host.getRight();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=%s.png".formatted(ServerUtils.getAddress(hostname, port)))
                .body(serverService.getServerFavicon(hostname, port));
    }
}