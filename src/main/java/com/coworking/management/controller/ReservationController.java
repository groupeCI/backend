package com.coworking.management.controller;

import com.coworking.management.dto.RecurringReservationRequest;
import com.coworking.management.dto.ReservationRequestDto;
import com.coworking.management.dto.ReservationResponseDto;
import com.coworking.management.entity.RecurrencePattern;
import com.coworking.management.entity.User;
import com.coworking.management.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/")
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto reservationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        ReservationResponseDto response = reservationService.createReservation(reservationRequest, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<ReservationResponseDto> updateReservationStatus(
            @PathVariable Long reservationId, @RequestParam String status) {
        ReservationResponseDto response = reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> response = reservationService.getAllReservations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        List<ReservationResponseDto> response = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("Reservation cancelled successfully");
    }
    
    
    @PostMapping("/recurring")
    public ResponseEntity<List<ReservationResponseDto>> createRecurringReservation(
        @RequestBody RecurringReservationRequest request,
        @RequestParam RecurrencePattern pattern,
        @RequestParam int occurrences) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        
        List<ReservationResponseDto> response = reservationService.createRecurringReservation(
            request.getReservationRequest(), 
            pattern,
            occurrences,
            userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/group")
    public ResponseEntity<List<ReservationResponseDto>> createGroupReservations(
            @RequestBody List<ReservationRequestDto> requests,
            @RequestParam Long teamSubscriptionId) {
        
        return ResponseEntity.ok(
            reservationService.createGroupReservations(requests, teamSubscriptionId)
        );
    }
    
    @GetMapping("/receptionniste/today")
    public ResponseEntity<List<ReservationResponseDto>> getTodayReservations() {
        return ResponseEntity.ok(reservationService.getTodayReservations());
    }
}