package com.coworking.management.repository;

import com.coworking.management.entity.BlackoutPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BlackoutPeriodRepository extends JpaRepository<BlackoutPeriod, Long> {
    List<BlackoutPeriod> findByEspaceIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Long espaceId, LocalDateTime endDate, LocalDateTime startDate);
    
    List<BlackoutPeriod> findByEspaceId(Long espaceId);
}