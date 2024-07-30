package xyz.mcutils.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.mcutils.backend.model.cache.CachedEndpointStatus;
import xyz.mcutils.backend.service.MojangService;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/mojang/", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Mojang Controller", description = "The Mojang Controller is used to get information about the Mojang APIs.")
public class MojangController {

    @Autowired
    private MojangService mojangService;

    @ResponseBody
    @GetMapping(value = "/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic())
                .body(mojangService.getMojangServerStatus());
    }
}
