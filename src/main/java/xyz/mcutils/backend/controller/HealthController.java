package xyz.mcutils.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
