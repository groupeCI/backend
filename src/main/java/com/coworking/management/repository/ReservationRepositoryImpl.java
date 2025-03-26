package com.coworking.management.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public double calculateUtilizationRate(Long spaceId, LocalDateTime start, LocalDateTime end) {
        String query = "SELECT SUM(TIMESTAMPDIFF(HOUR, r.start_date, r.end_date)) / (COUNT(DISTINCT DAY(r.start_date)) * 8.0) " +
                     "FROM reservations r WHERE r.espace_id = :spaceId AND r.start_date BETWEEN :start AND :end";
        
        return ((Number) em.createNativeQuery(query)
            .setParameter("spaceId", spaceId)
            .setParameter("start", start)
            .setParameter("end", end)
            .getSingleResult()).doubleValue();
    }
}