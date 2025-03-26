package com.coworking.management.repository;

import com.coworking.management.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByAvailable(boolean available);
    List<Event> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}