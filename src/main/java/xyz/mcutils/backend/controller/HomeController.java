package xyz.mcutils.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.mcutils.backend.config.Config;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    /**
     * The example UUID.
     */
    private final String exampleUuid = "eeab5f8a-18dd-4d58-af78-2b3c4543da48";
    private final String exampleJavaServer = "aetheria.cc";
    private final String exampleBedrockServer = "geo.hivebedrock.network";

    @GetMapping(value = "/")
    public String home(Model model) {
        model.addAttribute("public_url", Config.INSTANCE.getWebPublicUrl());
        model.addAttribute("player_example_url", Config.INSTANCE.getWebPublicUrl() + "/player/" + exampleUuid);
        model.addAttribute("java_server_example_url", Config.INSTANCE.getWebPublicUrl() + "/server/java/" + exampleJavaServer);
        model.addAttribute("bedrock_server_example_url", Config.INSTANCE.getWebPublicUrl() + "/server/bedrock/" + exampleBedrockServer);
        model.addAttribute("mojang_endpoint_status_url", Config.INSTANCE.getWebPublicUrl() + "/mojang/status");
        model.addAttribute("swagger_url", Config.INSTANCE.getWebPublicUrl() + "/swagger-ui.html");
        return "index";
    }
}
