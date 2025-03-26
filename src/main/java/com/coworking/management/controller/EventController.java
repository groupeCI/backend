package com.coworking.management.controller;

import com.coworking.management.dto.EventRegistrationDTO;
import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.Event;
import com.coworking.management.entity.User;
import com.coworking.management.repository.EventRepository;
import com.coworking.management.service.EventService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/")
    public ResponseEntity<ReqRes> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping("/")
    public ResponseEntity<ReqRes> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReqRes> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReqRes> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return ResponseEntity.ok(eventService.updateEvent(id, updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReqRes> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }
    
    @PostMapping("/register")
    public ResponseEntity<ReqRes> registerToEvent(@RequestBody EventRegistrationDTO registration) {
        return ResponseEntity.ok(eventService.registerToEvent(registration));
    }

    @PostMapping("/cancel-registration")
    public ResponseEntity<ReqRes> cancelRegistration(
            @RequestParam Long eventId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(eventService.cancelEventRegistration(eventId, userId));
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<User>> getEventParticipants(@PathVariable Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return ResponseEntity.ok(event.getParticipants());
    }
}