package com.coworking.management.service;

import com.coworking.management.entity.FinancialReport;
import com.coworking.management.repository.PaymentRepository;
import com.coworking.management.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;

    public Map<String, Object> getSpaceUtilizationReport(Long spaceId, LocalDate start, LocalDate end) {
        Map<String, Object> report = new HashMap<>();
        report.put("totalReservations", reservationRepository.countByEspaceIdAndStartDateBetween(
                spaceId, start.atStartOfDay(), end.atTime(23, 59, 59)));
        report.put("utilizationRate", reservationRepository.calculateUtilizationRate(
                spaceId, start.atStartOfDay(), end.atTime(23, 59, 59)));
        return report;
    }

    public FinancialReport getFinancialReport(int year, int month) {
        FinancialReport report = new FinancialReport();
        report.setTotalRevenue(paymentRepository.getTotalRevenue(year, month));
        report.setSubscriptionRevenue(paymentRepository.getSubscriptionRevenue(year, month));
        report.setOutstandingPayments(paymentRepository.getOutstandingAmount(year, month));
        return report;
    }
}