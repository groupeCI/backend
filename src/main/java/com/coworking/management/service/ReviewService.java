package com.coworking.management.service;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.dto.ReviewRequestDTO;
import com.coworking.management.dto.ReviewResponseDTO;
import com.coworking.management.entity.Espace;
import com.coworking.management.entity.Review;
import com.coworking.management.entity.RoleEnum;
import com.coworking.management.entity.User;
import com.coworking.management.repository.EspaceRepo;
import com.coworking.management.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EspaceRepo espaceRepository;

    public ReqRes createReview(ReviewRequestDTO reviewRequest) {
        ReqRes reqRes = new ReqRes();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Espace espace = espaceRepository.findById(reviewRequest.getEspaceId())
                    .orElseThrow(() -> new RuntimeException("Espace not found"));

            // Vérifier si l'utilisateur a déjà posté un avis pour cet espace
            if (reviewRepository.existsByUserIdAndEspaceId(currentUser.getId(), espace.getId())) {
                reqRes.setStatusCode(400);
                reqRes.setMessage("You have already reviewed this space");
                return reqRes;
            }

            Review review = new Review();
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());
            review.setUser(currentUser);
            review.setEspace(espace);
            
            // Pour les utilisateurs ADMIN, le review est automatiquement approuvé
            if (currentUser.getRole() == RoleEnum.ADMIN) {
                review.setApproved(true);
            }

            Review savedReview = reviewRepository.save(review);
            updateEspaceRating(espace);

            reqRes.setStatusCode(200);
            reqRes.setMessage("Review submitted successfully");
            reqRes.setReview(new ReviewResponseDTO(savedReview));
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error creating review: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes approveReview(Long reviewId) {
        ReqRes reqRes = new ReqRes();
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Review not found"));
            
            review.setApproved(true);
            Review approvedReview = reviewRepository.save(review);
            
            // Mettre à jour le rating de l'espace
            updateEspaceRating(approvedReview.getEspace());

            reqRes.setStatusCode(200);
            reqRes.setMessage("Review approved successfully");
            reqRes.setReview(new ReviewResponseDTO(approvedReview));
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error approving review: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getReviewsForEspace(Long espaceId, boolean approvedOnly) {
        ReqRes reqRes = new ReqRes();
        try {
            List<Review> reviews;
            if (approvedOnly) {
                reviews = reviewRepository.findByEspaceIdAndApproved(espaceId, true);
            } else {
                reviews = reviewRepository.findByEspaceId(espaceId);
            }
            
            List<ReviewResponseDTO> response = reviews.stream()
                    .map(ReviewResponseDTO::new)
                    .collect(Collectors.toList());
            
            reqRes.setStatusCode(200);
            reqRes.setMessage("Reviews retrieved successfully");
            reqRes.setReviewsList(response);
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error retrieving reviews: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getPendingReviews() {
        ReqRes reqRes = new ReqRes();
        try {
            List<Review> reviews = reviewRepository.findByApproved(false);
            List<ReviewResponseDTO> response = reviews.stream()
                    .map(ReviewResponseDTO::new)
                    .collect(Collectors.toList());
            
            reqRes.setStatusCode(200);
            reqRes.setMessage("Pending reviews retrieved successfully");
            reqRes.setReviewsList(response);
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error retrieving pending reviews: " + e.getMessage());
        }
        return reqRes;
    }

    private void updateEspaceRating(Espace espace) {
        List<Review> approvedReviews = reviewRepository.findByEspaceIdAndApproved(espace.getId(), true);
        if (!approvedReviews.isEmpty()) {
            double averageRating = approvedReviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            espace.setRating(Math.round(averageRating * 10.0) / 10.0); // Arrondir à 1 décimale
            espace.setReviewCount(approvedReviews.size());
            espaceRepository.save(espace);
        }
    }
}