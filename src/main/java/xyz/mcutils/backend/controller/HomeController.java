package xyz.mcutils.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.mcutils.backend.config.Config;

@Controller
@RequestMapping(value = "/")
public class HomeController {
    private final String examplePlayer = "Notch";
    private final String exampleJavaServer = "aetheria.cc";
    private final String exampleBedrockServer = "geo.hivebedrock.network";

    @GetMapping(value = "/")
    public String home(Model model) {
        String publicUrl = Config.INSTANCE.getWebPublicUrl();

        model.addAttribute("public_url", publicUrl);
        model.addAttribute("player_example_url", publicUrl + "/player/" + examplePlayer);
        model.addAttribute("java_server_example_url", publicUrl + "/server/java/" + exampleJavaServer);
        model.addAttribute("bedrock_server_example_url", publicUrl + "/server/bedrock/" + exampleBedrockServer);
        model.addAttribute("mojang_endpoint_status_url", publicUrl + "/mojang/status");
        model.addAttribute("swagger_url", publicUrl + "/swagger-ui.html");
        return "index";
    }
}
