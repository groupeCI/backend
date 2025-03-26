package com.coworking.management.repository;

import com.coworking.management.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByExpirationDateBetween(LocalDateTime start, LocalDateTime end);
    List<Subscription> findAll();
}