package com.coworking.management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.*;

import com.coworking.management.entity.Payment;
import com.coworking.management.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentTrackingService {
	@Autowired
    private PaymentRepository paymentRepository;
	
	@Autowired
    private EmailService emailService;

    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus("PENDING");
    }

    public List<Payment> getOverduePayments() {
        return paymentRepository.findOverduePayments(LocalDateTime.now().minusDays(3));
    }

    @Scheduled(cron = "0 0 9 * * ?") // Tous les jours à 9h
    public void sendPaymentReminders() {
        getOverduePayments().forEach(payment -> {
            emailService.sendConfirmationEmail(
                payment.getReservation().getUser().getEmail(),
                "Rappel de paiement en attente",
                "Votre paiement pour la réservation #" + payment.getReservation().getId() + " est en attente"
            );
        });
    }
}
