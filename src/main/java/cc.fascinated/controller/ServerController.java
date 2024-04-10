package cc.fascinated.controller;

import cc.fascinated.model.cache.CachedMinecraftServer;
import cc.fascinated.model.server.MinecraftServer;
import cc.fascinated.service.ServerService;
import cc.fascinated.service.pinger.impl.JavaMinecraftServerPinger;
import org.springframework.beans.factory.annotation.Autowired;
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
        String[] split = hostnameAndPort.split(":");
        String hostname = split[0];
        int port = 25565;
        if (split.length == 2) {
            try {
                port = Integer.parseInt(split[1]);
            } catch (NumberFormatException ignored) {}
        }
        return serverService.getServer(platform, hostname, port);
    }
}
