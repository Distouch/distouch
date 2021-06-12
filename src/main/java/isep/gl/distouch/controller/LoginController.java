package isep.gl.distouch.controller;

import isep.gl.distouch.model.User;
import isep.gl.distouch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "logout", required = false) boolean logout,
                            @RequestParam(value = "error", required = false) boolean error,
                            @RequestParam(value = "registered", required = false) boolean registered) {
        if (error) {
            model.addAttribute("message", "Invalid username or password");
            model.addAttribute("messageType", "error");
        }
        if (logout) {
            model.addAttribute("message", "Successfully logged out");
            model.addAttribute("messageType", "success");
        }
        if (registered) {
            model.addAttribute("message", "Successfully registered");
            model.addAttribute("messageType", "success");
        }
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveUser(user);
            redirectAttributes.addAttribute("registered", true);
            return "redirect:/login";
        }
        return "/registration";
    }
}
