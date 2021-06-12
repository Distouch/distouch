package isep.gl.distouch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "logout", required = false) boolean logout,
                            @RequestParam(value = "error", required = false) boolean error) {
        if (error) {
            model.addAttribute("message", "Invalid username or password");
            model.addAttribute("messageType", "error");
        }
        if (logout) {
            model.addAttribute("message", "Successfully logged out");
            model.addAttribute("messageType", "success");
        }
        return "login";
    }
}
