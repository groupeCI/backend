package com.coworking.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coworking.management.entity.Payment;
import com.coworking.management.service.PaymentTrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comptable/payments")
@RequiredArgsConstructor
public class PaymentTrackingController {
	@Autowired
    private PaymentTrackingService paymentTrackingService;

    @GetMapping("/pending")
    public ResponseEntity<List<Payment>> getPendingPayments() {
        return ResponseEntity.ok(paymentTrackingService.getPendingPayments());
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Payment>> getOverduePayments() {
        return ResponseEntity.ok(paymentTrackingService.getOverduePayments());
    }
}
