package com.coworking.management.service;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.Invoice;
import com.coworking.management.entity.Subscription;
import com.coworking.management.entity.SubscriptionStatus;
import com.coworking.management.entity.User;
import com.coworking.management.repository.SubscriptionRepository;
import com.coworking.management.repository.UserRepo;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	@Autowired
    private SubscriptionRepository subscriptionRepository;
	
	@Autowired
    private UserRepo userRepository;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
    private InvoiceService invoiceService;

    public ReqRes createSubscription(Subscription subscription) {
        subscription.setExpirationDate(LocalDateTime.now().plusDays(subscription.getDuration()));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        
        ReqRes response = new ReqRes();
        response.setStatusCode(200);
        response.setMessage("Subscription created successfully");
        response.setSubscription(savedSubscription);
        return response;
    }

    public ReqRes assignSubscriptionToUser(Long subscriptionId, Long userId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        
        user.setSubscription(subscription);
        userRepository.save(user);
        
        Invoice invoice = invoiceService.generateInvoice(subscription, user);
        
        ReqRes response = new ReqRes();
        response.setStatusCode(200);
        response.setMessage("Subscription assigned successfully");
        response.setSubscription(subscription);
        return response;
    }
    
    public ReqRes getAllSubscriptions() {
        ReqRes response = new ReqRes();
        try {
            List<Subscription> subscriptions = subscriptionRepository.findAll();
            response.setSubscriptionsList(subscriptions);
            response.setStatusCode(200);
            response.setMessage("Subscriptions retrieved successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving subscriptions: " + e.getMessage());
        }
        return response;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiringSubscriptions() {
        List<Subscription> expiringSubscriptions = subscriptionRepository
            .findByExpirationDateBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(3));
        
        expiringSubscriptions.forEach(sub -> {
            if(sub.isAutoRenew()) {
                renewSubscription(sub.getId(), true);
            } else {
                emailService.sendRenewalReminder(sub);
            }
        });
    }

    public ReqRes renewSubscription(Long subscriptionId, boolean autoRenew) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        subscription.setExpirationDate(subscription.getExpirationDate().plusDays(subscription.getDuration()));
        subscription.setAutoRenew(autoRenew);
        subscriptionRepository.save(subscription);
        
        ReqRes response = new ReqRes();
        response.setStatusCode(200);
        response.setMessage("Subscription renewed successfully");
        return response;
    }

    public ReqRes cancelSubscription(Long subscriptionId, String reason) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancellationDate(LocalDateTime.now());
        subscription.setCancellationReason(reason);
        subscriptionRepository.save(subscription);
        
        ReqRes response = new ReqRes();
        response.setStatusCode(200);
        response.setMessage("Subscription cancelled successfully");
        return response;
    }
}