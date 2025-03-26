package com.coworking.management.repository;

import com.coworking.management.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByReservationId(Long reservationId);
    List<Payment> findByStatus(String status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) = :month AND p.status = 'SUCCESS'")
    Double getTotalRevenue(int year, int month);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) = :month " +
    	       "AND p.status = 'SUCCESS' AND p.reservation.user.subscription IS NOT NULL")
    	Double getSubscriptionRevenue(int year, int month);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) = :month AND p.status = 'PENDING'")
    Double getOutstandingAmount(int year, int month);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.paymentDate < :cutoffDate")
    List<Payment> findOverduePayments(@Param("cutoffDate") LocalDateTime cutoffDate);
}