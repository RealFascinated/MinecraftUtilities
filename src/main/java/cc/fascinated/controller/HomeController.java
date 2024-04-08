package cc.fascinated.controller;

import cc.fascinated.config.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    /**
     * The example UUID.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final String exampleUuid = "eeab5f8a-18dd-4d58-af78-2b3c4543da48";

    @RequestMapping(value = "/")
    public String home(Model model) {
        model.addAttribute("url", Config.INSTANCE.getWebPublicUrl() + "/player/" + exampleUuid);
        model.addAttribute("avatar_url", Config.INSTANCE.getWebPublicUrl() + "/player/avatar/" + exampleUuid);
        return "index";
    }
}