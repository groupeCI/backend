package com.coworking.management.service;

import com.coworking.management.dto.PaymentRequestDto;
import com.coworking.management.dto.PaymentResponseDto;
import com.coworking.management.entity.Payment;
import com.coworking.management.entity.Reservation;
import com.coworking.management.exception.ResourceNotFoundException;
import com.coworking.management.repository.PaymentRepository;
import com.coworking.management.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequest) {
        Reservation reservation = reservationRepository.findById(paymentRequest.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus("PENDING");
        payment.setPaymentDate(LocalDateTime.now());

        // Simuler le traitement du paiement
        if (processPaymentGateway(paymentRequest)) {
            payment.setStatus("SUCCESS");
            reservation.setStatus("CONFIRMED");
            reservation.setPaymentId(String.valueOf(payment.getId()));
        } else {
            payment.setStatus("FAILED");
        }

        Payment savedPayment = paymentRepository.save(payment);
        reservationRepository.save(reservation);

        return mapToPaymentResponseDto(savedPayment);
    }

    private boolean processPaymentGateway(PaymentRequestDto paymentRequest) {
        // Simuler un appel à une passerelle de paiement
        return true; // Simuler un paiement réussi
    }

    private PaymentResponseDto mapToPaymentResponseDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setReservationId(payment.getReservation().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}