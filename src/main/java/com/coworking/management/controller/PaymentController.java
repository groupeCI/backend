package com.coworking.management.controller;

import com.coworking.management.dto.PaymentRequestDto;
import com.coworking.management.dto.PaymentResponseDto;
import com.coworking.management.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto paymentRequest) {
        PaymentResponseDto response = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(response);
    }
}