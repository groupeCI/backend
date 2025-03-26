package com.coworking.management.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coworking.management.entity.FinancialReport;
import com.coworking.management.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
	
	@Autowired
    private ReportService reportService;

    @GetMapping("/space-utilization/{spaceId}")
    public ResponseEntity<Map<String, Object>> getSpaceUtilization(
        @PathVariable Long spaceId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(reportService.getSpaceUtilizationReport(spaceId, start, end));
    }

    @GetMapping("/financial")
    public ResponseEntity<FinancialReport> getFinancialReport(
        @RequestParam int year,
        @RequestParam(required = false) Integer month) {
        return ResponseEntity.ok(reportService.getFinancialReport(year, month));
    }
}
