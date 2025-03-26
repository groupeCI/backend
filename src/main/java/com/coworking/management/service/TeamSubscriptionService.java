package com.coworking.management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coworking.management.entity.Subscription;
import com.coworking.management.entity.TeamSubscription;
import com.coworking.management.entity.User;
import com.coworking.management.repository.SubscriptionRepository;
import com.coworking.management.repository.TeamSubscriptionRepository;
import com.coworking.management.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamSubscriptionService {
	@Autowired
    private TeamSubscriptionRepository teamSubscriptionRepository;
	
	@Autowired
    private UserRepo userRepository;
	
	@Autowired
    private SubscriptionRepository subscriptionRepository;
	
	@Autowired
    private EmailService emailService;

    public TeamSubscription createTeamSubscription(Long subscriptionId, Long managerId, List<Long> memberIds) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        
        List<User> members = userRepository.findAllById(memberIds);
        
        TeamSubscription teamSubscription = new TeamSubscription();
        teamSubscription.setSubscription(subscription);
        teamSubscription.setManager(manager);
        teamSubscription.setMembers(members);
        teamSubscription.setStartDate(LocalDateTime.now());
        teamSubscription.setEndDate(LocalDateTime.now().plusDays(subscription.getDuration()));
        teamSubscription.setActive(true);
        
        // Notifier les membres
        members.forEach(member -> 
            emailService.sendConfirmationEmail(
                member.getEmail(),
                "Nouvel abonnement d'équipe",
                "Vous avez été ajouté à un abonnement d'équipe par " + manager.getFullName()
            )
        );
        
        return teamSubscriptionRepository.save(teamSubscription);
    }

    public List<TeamSubscription> getByManager(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return teamSubscriptionRepository.findByManager(manager);
    }
}
