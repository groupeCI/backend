package com.coworking.management.repository;

import java.time.LocalDateTime;

public interface ReservationRepositoryCustom {
    double calculateUtilizationRate(Long spaceId, LocalDateTime start, LocalDateTime end);
}


