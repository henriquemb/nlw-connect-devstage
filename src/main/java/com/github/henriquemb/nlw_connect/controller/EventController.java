package com.github.henriquemb.nlw_connect.controller;

import com.github.henriquemb.nlw_connect.model.Event;
import com.github.henriquemb.nlw_connect.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    @Autowired
    private EventService service;

    @PostMapping("/events")
    public Event addNewEvent(@RequestBody Event event) {
        return service.addNewEvent(event);
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
        Event event = service.getByPrettyName(prettyName);

        if (event != null) {
            return ResponseEntity.ok().body(event);
        }

        return ResponseEntity.notFound().build();
    }
}
