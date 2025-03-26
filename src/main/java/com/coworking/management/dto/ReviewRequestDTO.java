package com.coworking.management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequestDTO {
    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.5", message = "Rating must be at least 0.5")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Double rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;

    @NotNull(message = "Espace ID is required")
    private Long espaceId;
    
    

	public Long getEspaceId() {
		return espaceId;
	}

	public void setEspaceId(Long espaceId) {
		this.espaceId = espaceId;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
    
}