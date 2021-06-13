package isep.gl.distouch.controller;

import isep.gl.distouch.constants.MESSAGE;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
    @ModelAttribute("messages")
    public List<MESSAGE> addMessagesToModel(@ModelAttribute("messageId") ArrayList<String> messagesIds) {
        return messagesIds.stream().map(MESSAGE::valueOf).collect(Collectors.toList());
    }
}
