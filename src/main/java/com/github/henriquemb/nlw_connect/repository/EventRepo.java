package com.github.henriquemb.nlw_connect.repository;

import com.github.henriquemb.nlw_connect.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<Event, Integer> {
    Event findByPrettyName(String prettyName);
}
