package com.coworking.management.repository;

import com.coworking.management.entity.SpecialPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SpecialPriceRepository extends JpaRepository<SpecialPrice, Long> {
    List<SpecialPrice> findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date, LocalDate sameDate);
    List<SpecialPrice> findByEspaceIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long espaceId, LocalDate date, LocalDate sameDate);
    List<SpecialPrice> findBySubscriptionIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long subscriptionId, LocalDate date, LocalDate sameDate);
}