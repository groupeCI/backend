package com.coworking.management.repository;

import com.coworking.management.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByEspaceId(Long espaceId);
    List<Reservation> findByEspaceIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long espaceId, LocalDateTime endDate, LocalDateTime startDate);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.espace.id = :spaceId AND r.startDate BETWEEN :start AND :end")
    Long countByEspaceIdAndStartDateBetween(Long spaceId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT (COUNT(r) * 100.0 / (SELECT COUNT(r2) FROM Reservation r2 WHERE r2.espace.id = :spaceId)) " +
           "FROM Reservation r WHERE r.espace.id = :spaceId AND r.startDate BETWEEN :start AND :end")
    Double calculateUtilizationRate(Long spaceId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT r FROM Reservation r WHERE DATE(r.startDate) = CURRENT_DATE ORDER BY r.startDate")
    List<Reservation> findTodayReservations();
}