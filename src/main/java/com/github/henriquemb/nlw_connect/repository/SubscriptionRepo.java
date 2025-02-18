package com.github.henriquemb.nlw_connect.repository;

import com.github.henriquemb.nlw_connect.model.Event;
import com.github.henriquemb.nlw_connect.model.Subscription;
import com.github.henriquemb.nlw_connect.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
    Subscription findByEventAndSubscriber(Event event, User user);
}
