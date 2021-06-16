package isep.gl.distouch.controller;

import isep.gl.distouch.constants.MESSAGE;
import isep.gl.distouch.model.User;
import isep.gl.distouch.repository.UserRepository;
import isep.gl.distouch.service.UserService;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/login")
    public String loginPage(RedirectAttributes redirectAttributes,
                            @RequestParam(value = "logout", required = false) boolean logout,
                            @RequestParam(value = "error", required = false) boolean error) {
        List<String> messages = new ArrayList<>(List.of());
        if (error) messages.add(MESSAGE.LOGIN_FAIL.name());
        if (logout) messages.add(MESSAGE.LOGOUT_SUCCESS.name());
        if (error || logout) {
            redirectAttributes.addFlashAttribute("messageId", messages);
            return "redirect:login";
        }
        return "users/login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("messageId", MESSAGE.REGISTRATION_SUCCESS);
            return "redirect:login";
        }
        return "users/registration";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "users/profile";
    }

    @PostMapping("/profile")
    public String editUser(@Valid User editedUser, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            User currentUser = userService.getCurrentUser();
            BeanUtils.copyProperties(editedUser, currentUser, "id");
            userService.saveUser(currentUser);
            redirectAttributes.addFlashAttribute("messageId", MESSAGE.PROFILE_UPDATE_SUCCESS);
            return "redirect:profile"; //prevents form reload
        }
        return "users/profile";
    }

    @RequestMapping("/friends")
    public String friendsPage() {
        return "users/friends";
    }
}
