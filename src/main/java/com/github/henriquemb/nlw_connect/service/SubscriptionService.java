package com.github.henriquemb.nlw_connect.service;

import com.github.henriquemb.nlw_connect.dto.SubscriptionResponse;
import com.github.henriquemb.nlw_connect.exception.EventNotFoundException;
import com.github.henriquemb.nlw_connect.exception.SubscriptionConflictException;
import com.github.henriquemb.nlw_connect.exception.UserIndicatorNotFoundException;
import com.github.henriquemb.nlw_connect.model.Event;
import com.github.henriquemb.nlw_connect.model.Subscription;
import com.github.henriquemb.nlw_connect.model.User;
import com.github.henriquemb.nlw_connect.repository.EventRepo;
import com.github.henriquemb.nlw_connect.repository.SubscriptionRepo;
import com.github.henriquemb.nlw_connect.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {
        // Recuperar o evento
        Event event = eventRepo.findByPrettyName(eventName);

        if (event == null) {
            throw new EventNotFoundException("Evento " + eventName + " não existe");
        }

        // Salvar usuário
        User userResult = userRepo.findByEmail(user.getEmail());

        if (userResult == null) {
            userResult = userRepo.save(user);
        }

        // Busca usuário indicador
        User indicator = null;

        if (userId != null) {
            indicator = userRepo.findById(userId).orElse(null);

            if (indicator == null) {
                throw new UserIndicatorNotFoundException("Usuário indicador " + userId + " não existe");
            }
        }


        // Verificar inscrição
        Subscription subscriptionResult = subscriptionRepo.findByEventAndSubscriber(event, userResult);

        if (subscriptionResult != null) {
            throw new SubscriptionConflictException("Ja existe inscriÇão para o usuário " + userResult.getName() + " no evento " + eventName);
        }

        // Inscrever usuário
        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userResult);
        subscription.setIndication(indicator);

        Subscription result = subscriptionRepo.save(subscription);

        return new SubscriptionResponse(
                result.getSubscriptionNumber(), "http://localhost:8080/subscription/" +
                result.getEvent().getPrettyName() +
                "/" +
                result.getSubscriber().getId()
        );
    }
}
