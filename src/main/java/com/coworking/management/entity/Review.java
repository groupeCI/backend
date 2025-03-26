package com.coworking.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.5", message = "Rating must be at least 0.5")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Double rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;

    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "espace_id", nullable = false)
    private Espace espace;

    @Column(nullable = false)
    private boolean approved = false; // Par défaut non approuvé

    // Méthode pré-persist pour la date de création
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    //Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Espace getEspace() {
		return espace;
	}

	public void setEspace(Espace espace) {
		this.espace = espace;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}