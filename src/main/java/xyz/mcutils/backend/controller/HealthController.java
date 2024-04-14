package xyz.mcutils.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.mcutils.backend.config.Config;

import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class HealthController {

    @GetMapping(value = "/health")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(Map.of(
                "status", "OK"
        ));
    }
}
