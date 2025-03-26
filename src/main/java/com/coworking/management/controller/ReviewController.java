package com.coworking.management.controller;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.dto.ReviewRequestDTO;
import com.coworking.management.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<ReqRes> createReview(@RequestBody ReviewRequestDTO reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest));
    }

    @GetMapping("/espace/{espaceId}")
    public ResponseEntity<ReqRes> getEspaceReviews(
            @PathVariable Long espaceId,
            @RequestParam(required = false, defaultValue = "true") boolean approvedOnly) {
        return ResponseEntity.ok(reviewService.getReviewsForEspace(espaceId, approvedOnly));
    }

    @GetMapping("/pending")
    public ResponseEntity<ReqRes> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @PostMapping("/approve/{reviewId}")
    public ResponseEntity<ReqRes> approveReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.approveReview(reviewId));
    }
}