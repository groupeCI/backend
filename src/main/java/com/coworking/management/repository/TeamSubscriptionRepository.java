package com.coworking.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coworking.management.entity.TeamSubscription;
import com.coworking.management.entity.User;

public interface TeamSubscriptionRepository extends JpaRepository<TeamSubscription, Long> {
    List<TeamSubscription> findByManager(User manager);
    List<TeamSubscription> findByMembersContains(User member);
}
