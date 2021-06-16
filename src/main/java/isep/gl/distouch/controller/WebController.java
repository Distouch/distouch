package isep.gl.distouch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping({"/", "/index", "/home", "/homepage"})
    public String index() {
        return "index";
    }
}
