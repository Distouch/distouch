package isep.gl.distouch.controller;

import isep.gl.distouch.model.Event;
import isep.gl.distouch.model.User;
import isep.gl.distouch.repository.EventRepository;
import isep.gl.distouch.service.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserService userService;

    @RequestMapping("/feed")
    public String feed(Model model) {
        Iterable<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "/events/feed";
    }

    @GetMapping("/view/{event}")
    public String eventPage(Model model, @PathVariable Event event) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("event", event);
        model.addAttribute("isOrganizer", (currentUser == event.getOrganizer()));
        model.addAttribute("showAll", (event.getParticipants().contains(currentUser)));
        return "/events/view";
    }
    
    @GetMapping("/image/{event}")
    public void showEventImage(@PathVariable Event event, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(event.getImage());
        IOUtils.copy(is, response.getOutputStream());
    }
}
