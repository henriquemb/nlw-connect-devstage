package com.github.henriquemb.nlw_connect.service;

import com.github.henriquemb.nlw_connect.dto.SubscriptionRankingByUser;
import com.github.henriquemb.nlw_connect.dto.SubscriptionRankingItem;
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

import java.util.List;

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

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        Event event = eventRepo.findByPrettyName(prettyName);

        if (event == null) {
            throw new EventNotFoundException("Ranking do evento " + prettyName + " não existe");
        }

        return subscriptionRepo.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        User user = userRepo.findById(userId).orElse(null);

        if (user == null) {
            throw new UserIndicatorNotFoundException("Usuário " + userId + " não existe");
        }

        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);

        SubscriptionRankingItem item = ranking.stream()
                .filter(r -> r.userId().equals(userId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            throw new UserIndicatorNotFoundException("Não há inscrições com indicação para o usuário " + userId);
        }

        int position = ranking.indexOf(item) + 1;

        return new SubscriptionRankingByUser(position, item);
    }
}
