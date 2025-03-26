package com.coworking.management.service;

import com.coworking.management.dto.ReservationRequestDto;
import com.coworking.management.dto.ReservationResponseDto;
import com.coworking.management.entity.BlackoutPeriod;
import com.coworking.management.entity.Espace;
import com.coworking.management.entity.RecurrencePattern;
import com.coworking.management.entity.Reservation;
import com.coworking.management.entity.TeamSubscription;
import com.coworking.management.entity.User;
import com.coworking.management.exception.ResourceNotFoundException;
import com.coworking.management.exception.ReservationNotAvailableException;
import com.coworking.management.repository.BlackoutPeriodRepository;
import com.coworking.management.repository.EspaceRepo;
import com.coworking.management.repository.ReservationRepository;
import com.coworking.management.repository.TeamSubscriptionRepository;
import com.coworking.management.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EspaceRepo espaceRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BlackoutPeriodRepository blackoutPeriodRepository;
    
    @Autowired
    private TeamSubscriptionRepository teamSubscriptionRepository;

    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequest, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Espace espace = espaceRepository.findById(reservationRequest.getEspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Espace not found"));

        // Vérifier si l'utilisateur a un abonnement valide
        if (user.getSubscription() == null) {
            throw new IllegalArgumentException("User does not have a valid subscription");
        }

        // Vérifier que la date de début est avant la date de fin
        if (reservationRequest.getStartDate().isAfter(reservationRequest.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Vérifier la disponibilité de l'espace
        if (!isEspaceAvailable(espace.getId(), reservationRequest.getStartDate(), reservationRequest.getEndDate())) {
            throw new ReservationNotAvailableException("Espace not available for the selected dates");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEspace(espace);
        reservation.setStartDate(reservationRequest.getStartDate());
        reservation.setEndDate(reservationRequest.getEndDate());
        reservation.setStatus("PENDING");

        Reservation savedReservation = reservationRepository.save(reservation);

        // Envoyer un email de confirmation
        emailService.sendConfirmationEmail(
            user.getEmail(),
            "Confirmation de Réservation",
            "Votre réservation pour l'espace " + espace.getTitle() + " a été confirmée."
        );

        return mapToReservationResponseDto(savedReservation);
    }

    public ReservationResponseDto updateReservationStatus(Long reservationId, String status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        // Vérifier que le statut est valide
        if (!List.of("PENDING", "ACCEPTED", "REJECTED", "CANCELLED").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }

        reservation.setStatus(status);
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Envoyer un email de notification
        emailService.sendConfirmationEmail(
            reservation.getUser().getEmail(),
            "Mise à jour de Réservation",
            "Le statut de votre réservation pour l'espace " + reservation.getEspace().getTitle() + " est maintenant " + status + "."
        );

        return mapToReservationResponseDto(updatedReservation);
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToReservationResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::mapToReservationResponseDto)
                .collect(Collectors.toList());
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);

        // Envoyer un email d'annulation
        emailService.sendConfirmationEmail(
            reservation.getUser().getEmail(),
            "Annulation de Réservation",
            "Votre réservation pour l'espace " + reservation.getEspace().getTitle() + " a été annulée."
        );
    }

    private ReservationResponseDto mapToReservationResponseDto(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setEspaceId(reservation.getEspace().getId());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setStatus(reservation.getStatus());
        dto.setPaymentId(reservation.getPaymentId());
        return dto;
    }
    
    public boolean isEspaceAvailable(Long espaceId, LocalDateTime startDate, LocalDateTime endDate) {
        // Vérifier les périodes de blackout
        List<BlackoutPeriod> blackoutPeriods = blackoutPeriodRepository
            .findByEspaceIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                espaceId, endDate, startDate);
        
        if (!blackoutPeriods.isEmpty()) {
            return false;
        }

        // Vérifier les réservations existantes
        List<Reservation> overlappingReservations = reservationRepository
            .findByEspaceIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                espaceId, endDate, startDate);

        return overlappingReservations.stream()
            .noneMatch(reservation -> !reservation.getStatus().equals("CANCELLED") 
                                  && !reservation.getStatus().equals("REJECTED"));
    }

    // Nouvelle méthode pour les réservations récurrentes
    public List<ReservationResponseDto> createRecurringReservation(
    	    ReservationRequestDto request, 
    	    RecurrencePattern pattern,
    	    int occurrences,
    	    Long userId) {
    	    
    	    List<ReservationResponseDto> createdReservations = new ArrayList<>();
    	    
    	    LocalDateTime currentStart = request.getStartDate();
    	    LocalDateTime currentEnd = request.getEndDate();
    	    
    	    for (int i = 0; i < occurrences; i++) {
    	        if (isEspaceAvailable(request.getEspaceId(), currentStart, currentEnd)) {
    	            ReservationRequestDto currentRequest = new ReservationRequestDto();
    	            currentRequest.setEspaceId(request.getEspaceId());
    	            currentRequest.setStartDate(currentStart);
    	            currentRequest.setEndDate(currentEnd);
    	            
    	            createdReservations.add(createReservation(currentRequest, userId));
    	        }
    	        
    	        // Calculer les prochaines dates selon le motif de récurrence
    	        switch (pattern) {
    	            case DAILY:
    	                currentStart = currentStart.plusDays(1);
    	                currentEnd = currentEnd.plusDays(1);
    	                break;
    	            case WEEKLY:
    	                currentStart = currentStart.plusWeeks(1);
    	                currentEnd = currentEnd.plusWeeks(1);
    	                break;
    	            case MONTHLY:
    	                currentStart = currentStart.plusMonths(1);
    	                currentEnd = currentEnd.plusMonths(1);
    	                break;
    	        }
    	    }
    	    
    	    return createdReservations;
    	}
    
    public List<ReservationResponseDto> createGroupReservations(
            List<ReservationRequestDto> requests, 
            Long teamSubscriptionId) {
        
        TeamSubscription teamSubscription = teamSubscriptionRepository.findById(teamSubscriptionId)
                .orElseThrow(() -> new RuntimeException("Team subscription not found"));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User manager = (User) authentication.getPrincipal();
        
        if (!teamSubscription.getManager().equals(manager)) {
            throw new RuntimeException("Only team manager can make group reservations");
        }
        
        return requests.stream()
                .map(request -> {
                    ReservationRequestDto modifiedRequest = new ReservationRequestDto();
                    modifiedRequest.setEspaceId(request.getEspaceId());
                    modifiedRequest.setStartDate(request.getStartDate());
                    modifiedRequest.setEndDate(request.getEndDate());
                    
                    // Créer une réservation pour chaque membre
                    return teamSubscription.getMembers().stream()
                            .map(member -> createReservation(modifiedRequest, member.getId()))
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    
    public List<ReservationResponseDto> getTodayReservations() {
        return reservationRepository.findTodayReservations().stream()
                .map(this::mapToReservationResponseDto)
                .collect(Collectors.toList());
    }
}