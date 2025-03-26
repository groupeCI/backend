package com.coworking.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.coworking.management.entity.TeamSubscription;
import com.coworking.management.entity.User;
import com.coworking.management.service.TeamSubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entreprise/team-subscriptions")
@RequiredArgsConstructor
public class TeamSubscriptionController {
	@Autowired
    private TeamSubscriptionService teamSubscriptionService;

    @PostMapping("/")
    public ResponseEntity<TeamSubscription> createTeamSubscription(
            @RequestParam Long subscriptionId,
            @RequestParam List<Long> memberIds) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long managerId = ((User) authentication.getPrincipal()).getId();
        
        return ResponseEntity.ok(
            teamSubscriptionService.createTeamSubscription(subscriptionId, managerId, memberIds)
        );
    }

    @GetMapping("/my-teams")
    public ResponseEntity<List<TeamSubscription>> getMyTeamSubscriptions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long managerId = ((User) authentication.getPrincipal()).getId();
        
        return ResponseEntity.ok(teamSubscriptionService.getByManager(managerId));
    }
}