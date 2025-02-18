package com.github.henriquemb.nlw_connect.controller;

import com.github.henriquemb.nlw_connect.dto.ErrorMessage;
import com.github.henriquemb.nlw_connect.dto.SubscriptionResponse;
import com.github.henriquemb.nlw_connect.exception.EventNotFoundException;
import com.github.henriquemb.nlw_connect.exception.SubscriptionConflictException;
import com.github.henriquemb.nlw_connect.exception.UserIndicatorNotFoundException;
import com.github.henriquemb.nlw_connect.model.User;
import com.github.henriquemb.nlw_connect.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService service;

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName, @RequestBody User subscriber, @PathVariable(required = false) Integer userId) {
        try {
            SubscriptionResponse subscription = service.createNewSubscription(prettyName, subscriber, userId);

            if (subscription != null) {
                return ResponseEntity.ok(subscription);
            }
        }
        catch (EventNotFoundException | UserIndicatorNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
        catch (SubscriptionConflictException e) {
            return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));
        }

        return ResponseEntity.badRequest().build();
    }
}
