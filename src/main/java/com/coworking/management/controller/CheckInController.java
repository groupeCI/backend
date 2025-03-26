package com.coworking.management.controller;

import com.coworking.management.entity.CheckInLog;
import com.coworking.management.service.CheckInService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receptionniste/checkin")
@RequiredArgsConstructor
public class CheckInController {
	@Autowired
    private CheckInService checkInService;

    @PostMapping("/{reservationId}")
    public ResponseEntity<CheckInLog> checkIn(
            @PathVariable Long reservationId,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(checkInService.checkIn(reservationId, notes));
    }

    @PostMapping("/{reservationId}/checkout")
    public ResponseEntity<CheckInLog> checkOut(@PathVariable Long reservationId) {
        return ResponseEntity.ok(checkInService.checkOut(reservationId));
    }

    @GetMapping("/history/{reservationId}")
    public ResponseEntity<List<CheckInLog>> getCheckInHistory(@PathVariable Long reservationId) {
        return ResponseEntity.ok(checkInService.getCheckInHistory(reservationId));
    }
}