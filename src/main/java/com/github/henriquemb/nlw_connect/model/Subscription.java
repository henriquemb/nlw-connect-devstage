package com.github.henriquemb.nlw_connect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subscription")
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_number")
    private Integer subscriptionNumber;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "subscribed_user_id", nullable = false)
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "indication_user_id", nullable = true)
    private User indication;
}
