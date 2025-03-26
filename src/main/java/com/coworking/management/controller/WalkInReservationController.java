package com.coworking.management.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coworking.management.entity.Reservation;
import com.coworking.management.service.WalkInReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receptionniste/walkin")
@RequiredArgsConstructor
public class WalkInReservationController {
	@Autowired
    private WalkInReservationService walkInReservationService;

    @PostMapping("/")
    public ResponseEntity<Reservation> createWalkInReservation(
            @RequestParam Long espaceId,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String userEmail) {
        
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        
        return ResponseEntity.ok(
            walkInReservationService.createWalkInReservation(
                espaceId, startTime, endTime, userEmail)
        );
    }
}
