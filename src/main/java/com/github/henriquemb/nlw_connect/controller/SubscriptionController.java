package com.github.henriquemb.nlw_connect.controller;

import com.github.henriquemb.nlw_connect.dto.ErrorMessage;
import com.github.henriquemb.nlw_connect.dto.SubscriptionRankingByUser;
import com.github.henriquemb.nlw_connect.dto.SubscriptionRankingItem;
import com.github.henriquemb.nlw_connect.dto.SubscriptionResponse;
import com.github.henriquemb.nlw_connect.exception.EventNotFoundException;
import com.github.henriquemb.nlw_connect.exception.SubscriptionConflictException;
import com.github.henriquemb.nlw_connect.exception.UserIndicatorNotFoundException;
import com.github.henriquemb.nlw_connect.model.User;
import com.github.henriquemb.nlw_connect.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName) {
        try {
            List<SubscriptionRankingItem> ranking = service.getCompleteRanking(prettyName);

            if (ranking.size() >= 3) {
                ranking = ranking.subList(0, 3);
            }

            return ResponseEntity.ok(ranking);
        }
        catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName, @PathVariable Integer userId) {
        try {
            SubscriptionRankingByUser ranking = service.getRankingByUser(prettyName, userId);

            return ResponseEntity.ok(ranking);
        }
        catch (UserIndicatorNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }
}
