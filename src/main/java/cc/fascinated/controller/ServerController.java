package cc.fascinated.controller;

import cc.fascinated.model.server.MinecraftServer;
import cc.fascinated.service.pinger.impl.JavaMinecraftServerPinger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/server/")
public class ServerController {

    @ResponseBody
    @GetMapping(value = "/{hostname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinecraftServer> getServer(@PathVariable String hostname) {
        return ResponseEntity.ok(JavaMinecraftServerPinger.INSTANCE.ping(hostname, 25565));
    }
}
