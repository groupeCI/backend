package com.coworking.management.service;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.SpecialPrice;
import com.coworking.management.repository.SpecialPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PricingService {

    @Autowired
    private SpecialPriceRepository specialPriceRepository;

    public ReqRes createSpecialPrice(SpecialPrice specialPrice) {
        ReqRes response = new ReqRes();
        try {
            SpecialPrice savedPrice = specialPriceRepository.save(specialPrice);
            response.setStatusCode(200);
            response.setMessage("Special price created successfully");
            response.setSpecialPrice(savedPrice);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating special price: " + e.getMessage());
        }
        return response;
    }

    public List<SpecialPrice> getCurrentPromotions() {
        LocalDate today = LocalDate.now();
        return specialPriceRepository.findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }

    public double calculateDiscountedPrice(double originalPrice, Long espaceId, Long subscriptionId) {
        LocalDate today = LocalDate.now();
        List<SpecialPrice> applicablePrices = specialPriceRepository
                .findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
        
        double finalPrice = originalPrice;
        
        for (SpecialPrice price : applicablePrices) {
            if ((price.getEspace() == null || price.getEspace().getId().equals(espaceId)) &&
                (price.getSubscription() == null || price.getSubscription().getId().equals(subscriptionId))) {
                
                if (price.getDiscountType() == SpecialPrice.DiscountType.PERCENTAGE) {
                    finalPrice -= originalPrice * (price.getDiscountPercentage() / 100);
                } else {
                    finalPrice -= price.getDiscountPercentage();
                }
            }
        }
        
        return Math.max(finalPrice, 0); // Le prix ne peut pas être négatif
    }
}