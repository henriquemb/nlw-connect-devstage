package com.github.henriquemb.nlw_connect.service;

import com.github.henriquemb.nlw_connect.model.Event;
import com.github.henriquemb.nlw_connect.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepo repository;

    public Event addNewEvent(Event event) {
        // Gerando o pretty name
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return repository.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) repository.findAll();
    }

    public Event getByPrettyName(String prettyName) {
        return repository.findByPrettyName(prettyName);
    }
}
