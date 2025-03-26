package com.coworking.management.service;

import com.coworking.management.entity.CheckInLog;
import com.coworking.management.entity.Reservation;
import com.coworking.management.entity.User;
import com.coworking.management.repository.CheckInLogRepository;
import com.coworking.management.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInService {
	@Autowired
    private CheckInLogRepository checkInLogRepository;
	
	@Autowired
    private ReservationRepository reservationRepository;
	
	@Autowired
    private NotificationService notificationService;

    public CheckInLog checkIn(Long reservationId, String notes) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        User receptionist = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Vérifier s'il n'y a pas déjà un check-in actif
        checkInLogRepository.findByReservationIdAndCheckOutTimeIsNull(reservationId)
                .ifPresent(log -> {
                    throw new RuntimeException("Un check-in actif existe déjà pour cette réservation");
                });
        
        CheckInLog log = new CheckInLog();
        log.setReservation(reservation);
        log.setReceptionist(receptionist);
        log.setCheckInTime(LocalDateTime.now());
        log.setNotes(notes);
        
        // Notifier l'utilisateur
        notificationService.createNotification(
            reservation.getUser(),
            "Check-in confirmé",
            "Votre check-in pour " + reservation.getEspace().getTitle() + " a été enregistré",
            true
        );
        
        return checkInLogRepository.save(log);
    }

    public CheckInLog checkOut(Long reservationId) {
        CheckInLog log = checkInLogRepository.findByReservationIdAndCheckOutTimeIsNull(reservationId)
                .orElseThrow(() -> new RuntimeException("No active check-in found"));
        
        log.setCheckOutTime(LocalDateTime.now());
        
        // Notifier l'utilisateur
        notificationService.createNotification(
            log.getReservation().getUser(),
            "Check-out confirmé",
            "Votre check-out pour " + log.getReservation().getEspace().getTitle() + " a été enregistré",
            true
        );
        
        return checkInLogRepository.save(log);
    }

    public List<CheckInLog> getCheckInHistory(Long reservationId) {
        return checkInLogRepository.findByReservationId(reservationId);
    }
}