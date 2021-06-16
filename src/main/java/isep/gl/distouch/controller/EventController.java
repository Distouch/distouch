package isep.gl.distouch.controller;

import isep.gl.distouch.constants.MESSAGE;
import isep.gl.distouch.model.Event;
import isep.gl.distouch.model.User;
import isep.gl.distouch.repository.EventRepository;
import isep.gl.distouch.service.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

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
        return "events/feed";
    }

    @GetMapping("/create")
    public String newEventPage(Model model) {
        Event event = new Event();
        User currentUser = userService.getCurrentUser();
        event.setOrganizer(currentUser);
        event.setParticipants(Set.of(currentUser));
        model.addAttribute("event", event);
        return "events/create";
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public String createEvent(@Valid Event event, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<Event> existingEvent = eventRepository.findByTitle(event.getTitle());
        if (existingEvent.isPresent()) {
            bindingResult
                    .rejectValue("title", "error.event",
                            "There is already an event registered with the title provided");
        }
        if (!bindingResult.hasErrors()) {
            eventRepository.save(event);
            redirectAttributes.addFlashAttribute("messageId", MESSAGE.EVENT_CREATION_SUCCESS);
            return "redirect:events/view/" + event.getId();
        }
        return "events/create";
    }

    @GetMapping("/view/{event}")
    public String eventPage(Model model, @PathVariable Event event) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("event", event);
        model.addAttribute("isOrganizer", (currentUser == event.getOrganizer()));
        model.addAttribute("showAll", (event.getParticipants().contains(currentUser)));
        return "events/view";
    }

    @GetMapping("/edit/{event}")
    public String editEventPage(@PathVariable Event event, Model model) {
        model.addAttribute("event", event);
        return "events/edit";
    }

    @PostMapping("/edit/{event}")
    public String editEvent(@PathVariable Event event, @Valid Event editedEvent,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            BeanUtils.copyProperties(editedEvent, event, "id");
            eventRepository.save(event);
            redirectAttributes.addFlashAttribute("messageId", MESSAGE.EVENT_UPDATE_SUCCESS);
            return "redirect:events/view/" + event.getId();
        }
        return "events/edit";
    }

    @GetMapping("/calendar")
    public String calendarPage() {
        return "events/calendar";
    }

    @GetMapping("/image/{event}")
    public void showEventImage(@PathVariable Event event, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(event.getImage());
        IOUtils.copy(is, response.getOutputStream());
    }
}
