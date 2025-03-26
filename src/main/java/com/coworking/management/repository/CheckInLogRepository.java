package com.coworking.management.repository;

import com.coworking.management.entity.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInLogRepository extends JpaRepository<CheckInLog, Long> {
    
    // Trouver le check-in actif pour une réservation (sans check-out)
    Optional<CheckInLog> findByReservationIdAndCheckOutTimeIsNull(Long reservationId);
    
    // Trouver tous les check-ins pour une réservation
    List<CheckInLog> findByReservationId(Long reservationId);
}