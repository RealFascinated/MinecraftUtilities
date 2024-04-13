package cc.fascinated.controller;

import cc.fascinated.model.cache.CachedEndpointStatus;
import cc.fascinated.service.MojangService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Mojang Controller", description = "The Mojang Controller is used to get information about the Mojang APIs.")
@RequestMapping(value = "/mojang/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MojangController {

    @Autowired
    private MojangService mojangService;

    @ResponseBody
    @RequestMapping(value = "/status")
    public CachedEndpointStatus getStatus() {
        return mojangService.getMojangApiStatus();
    }
}
