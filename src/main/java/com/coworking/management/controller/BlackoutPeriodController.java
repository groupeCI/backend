package com.coworking.management.controller;

import com.coworking.management.entity.BlackoutPeriod;
import com.coworking.management.repository.BlackoutPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blackout-periods")
public class BlackoutPeriodController {

    @Autowired
    private BlackoutPeriodRepository blackoutPeriodRepository;

    @PostMapping("/")
    public BlackoutPeriod createBlackoutPeriod(@RequestBody BlackoutPeriod blackoutPeriod) {
        return blackoutPeriodRepository.save(blackoutPeriod);
    }

    @GetMapping("/espace/{espaceId}")
    public List<BlackoutPeriod> getBlackoutPeriodsForEspace(@PathVariable Long espaceId) {
        return blackoutPeriodRepository.findByEspaceId(espaceId);
    }

    @DeleteMapping("/{id}")
    public void deleteBlackoutPeriod(@PathVariable Long id) {
        blackoutPeriodRepository.deleteById(id);
    }
}