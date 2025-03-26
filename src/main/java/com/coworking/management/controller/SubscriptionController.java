package com.coworking.management.controller;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.Subscription;
import com.coworking.management.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/")
    public ResponseEntity<ReqRes> createSubscription(@RequestBody Subscription subscription) {
        return ResponseEntity.ok(subscriptionService.createSubscription(subscription));
    }

    @GetMapping("/")
    public ResponseEntity<ReqRes> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }
}