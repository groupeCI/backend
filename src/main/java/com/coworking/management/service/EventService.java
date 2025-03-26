package com.coworking.management.service;

import com.coworking.management.dto.EventRegistrationDTO;
import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.Event;
import com.coworking.management.entity.User;
import com.coworking.management.repository.EventRepository;
import com.coworking.management.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private EmailService emailService ;
    
    @Autowired
    private PricingService pricingService ;

    public ReqRes createEvent(Event event) {
        ReqRes response = new ReqRes();
        try {
            Event savedEvent = eventRepository.save(event);
            response.setStatusCode(200);
            response.setMessage("Event created successfully");
            response.setEvent(savedEvent);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating event: " + e.getMessage());
        }
        return response;
    }

    public ReqRes getAllEvents() {
        ReqRes response = new ReqRes();
        try {
            List<Event> events = eventRepository.findAll();
            response.setStatusCode(200);
            response.setMessage("Events retrieved successfully");
            response.setEventsList(events);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving events: " + e.getMessage());
        }
        return response;
    }

    public ReqRes getEventById(Long id) {
        ReqRes response = new ReqRes();
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            response.setStatusCode(200);
            response.setMessage("Event retrieved successfully");
            response.setEvent(event);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving event: " + e.getMessage());
        }
        return response;
    }

    public ReqRes updateEvent(Long id, Event updatedEvent) {
        ReqRes response = new ReqRes();
        try {
            Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            existingEvent.setTitle(updatedEvent.getTitle());
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setStartDate(updatedEvent.getStartDate());
            existingEvent.setEndDate(updatedEvent.getEndDate());
            existingEvent.setLocation(updatedEvent.getLocation());
            existingEvent.setPrice(updatedEvent.getPrice());
            existingEvent.setCapacity(updatedEvent.getCapacity());
            existingEvent.setAvailable(updatedEvent.isAvailable());

            Event savedEvent = eventRepository.save(existingEvent);
            response.setStatusCode(200);
            response.setMessage("Event updated successfully");
            response.setEvent(savedEvent);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating event: " + e.getMessage());
        }
        return response;
    }

    public ReqRes deleteEvent(Long id) {
        ReqRes response = new ReqRes();
        try {
            eventRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("Event deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting event: " + e.getMessage());
        }
        return response;
    }
    
 // Ajouter ces méthodes à EventService

    public ReqRes registerToEvent(EventRegistrationDTO registration) {
        ReqRes response = new ReqRes();
        try {
            Event event = eventRepository.findById(registration.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            
            User user = userRepo.findById(registration.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Vérifier si l'événement est complet
            if (event.getParticipants().size() >= event.getMaxParticipants()) {
                response.setStatusCode(400);
                response.setMessage("This event is already full");
                return response;
            }

            // Vérifier si l'utilisateur est déjà inscrit
            if (event.getParticipants().contains(user)) {
                response.setStatusCode(400);
                response.setMessage("User is already registered for this event");
                return response;
            }

            // Appliquer un discount si l'utilisateur a un abonnement et demande le discount
            double finalPrice = event.getPrice();
            if (registration.isApplyDiscount() && user.getSubscription() != null) {
                finalPrice = pricingService.calculateDiscountedPrice(
                    event.getPrice(), 
                    null, 
                    user.getSubscription().getId());
            }

            event.getParticipants().add(user);
            eventRepository.save(event);

            // Envoyer une confirmation par email
            emailService.sendConfirmationEmail(
                user.getEmail(),
                "Confirmation d'inscription à l'événement",
                "Vous êtes inscrit à l'événement " + event.getTitle() + 
                ". Prix final: " + finalPrice + "€"
            );

            response.setStatusCode(200);
            response.setMessage("Successfully registered to event");
            response.setEvent(event);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error registering to event: " + e.getMessage());
        }
        return response;
    }

    public ReqRes cancelEventRegistration(Long eventId, Long userId) {
        ReqRes response = new ReqRes();
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!event.getParticipants().contains(user)) {
                response.setStatusCode(400);
                response.setMessage("User is not registered for this event");
                return response;
            }

            event.getParticipants().remove(user);
            eventRepository.save(event);

            emailService.sendConfirmationEmail(
                user.getEmail(),
                "Annulation d'inscription",
                "Votre inscription à l'événement " + event.getTitle() + " a été annulée."
            );

            response.setStatusCode(200);
            response.setMessage("Successfully canceled event registration");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error canceling registration: " + e.getMessage());
        }
        return response;
    }
}