package isep.gl.distouch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationsController {
    @RequestMapping
    public String notificationsPage() {
        return "/notifications";
    }
}
