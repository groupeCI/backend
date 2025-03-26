package com.coworking.management.controller;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.SpecialPrice;
import com.coworking.management.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @PostMapping("/special-price")
    public ResponseEntity<ReqRes> createSpecialPrice(@RequestBody SpecialPrice specialPrice) {
        return ResponseEntity.ok(pricingService.createSpecialPrice(specialPrice));
    }

    @GetMapping("/current-promotions")
    public ResponseEntity<List<SpecialPrice>> getCurrentPromotions() {
        return ResponseEntity.ok(pricingService.getCurrentPromotions());
    }

    @GetMapping("/calculate-price")
    public ResponseEntity<Double> calculatePrice(
            @RequestParam double originalPrice,
            @RequestParam(required = false) Long espaceId,
            @RequestParam(required = false) Long subscriptionId) {
        return ResponseEntity.ok(pricingService.calculateDiscountedPrice(
                originalPrice, espaceId, subscriptionId));
    }
}