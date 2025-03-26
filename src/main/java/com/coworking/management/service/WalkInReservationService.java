package com.coworking.management.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import com.coworking.management.entity.Espace;
import com.coworking.management.entity.Reservation;
import com.coworking.management.entity.User;
import com.coworking.management.repository.EspaceRepo;
import com.coworking.management.repository.ReservationRepository;
import com.coworking.management.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalkInReservationService {
	@Autowired
    private ReservationRepository reservationRepository;
	
	@Autowired
    private EspaceRepo espaceRepository;
	
	@Autowired
    private UserRepo userRepository;

    public Reservation createWalkInReservation(
            Long espaceId,
            LocalDateTime start,
            LocalDateTime end,
            String userEmail) {
        
        Espace espace = espaceRepository.findById(espaceId)
                .orElseThrow(() -> new RuntimeException("Espace not found"));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Reservation reservation = new Reservation();
        reservation.setEspace(espace);
        reservation.setUser(user);
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setStatus("WALK_IN");
        
        return reservationRepository.save(reservation);
    }
}