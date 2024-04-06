package cc.fascinated.api.controller;

import cc.fascinated.Consts;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "/")
    public String home(Model model) {
        model.addAttribute("url", Consts.getSITE_URL() + "/eeab5f8a-18dd-4d58-af78-2b3c4543da48");
        model.addAttribute("avatar_url", Consts.getSITE_URL() + "/avatar/eeab5f8a-18dd-4d58-af78-2b3c4543da48");
        return "index";
    }
}
