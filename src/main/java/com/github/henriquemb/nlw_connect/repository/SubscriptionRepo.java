package com.github.henriquemb.nlw_connect.repository;

import com.github.henriquemb.nlw_connect.dto.SubscriptionRankingItem;
import com.github.henriquemb.nlw_connect.model.Event;
import com.github.henriquemb.nlw_connect.model.Subscription;
import com.github.henriquemb.nlw_connect.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
    Subscription findByEventAndSubscriber(Event event, User user);

    @Query(value = "SELECT u.user_id, u.user_name, COUNT(s.subscription_number) AS Qtd " +
            "FROM subscription s " +
            "INNER JOIN user u " +
            "    ON s.indication_user_id = u.user_id " +
            "WHERE s.event_id = :event_id AND s.indication_user_id IS NOT NULL " +
            "GROUP BY s.indication_user_id " +
            "ORDER BY Qtd DESC", nativeQuery = true)
    List<SubscriptionRankingItem> generateRanking(@Param("event_id") Integer eventId);
}
