package com.coworking.management.dto;

import com.coworking.management.entity.Review;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Double rating;
    private String comment;
    private Date createdAt;
    private UserDTO user;
    private Long espaceId;
    private boolean approved;

    public ReviewResponseDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
        this.user = new UserDTO(review.getUser());
        this.espaceId = review.getEspace().getId();
        this.approved = review.isApproved();
    }
}
