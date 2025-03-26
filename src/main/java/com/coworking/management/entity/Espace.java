package com.coworking.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data 
@NoArgsConstructor 
@Entity
@Table(name = "espaces")
public class Espace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Image URL cannot be blank")
    @Column(nullable = false)
    private String image;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 2500, message = "Description cannot exceed 2500 characters")
    @Column(nullable = false, length = 2500)
    private String description;

    @NotBlank(message = "Location cannot be blank")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String localisation;

    @PositiveOrZero(message = "Price must be a positive number or zero")
    @Column(nullable = false)
    private double price;

    @Positive(message = "Capacity must be a positive number")
    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private boolean available;

    @NotBlank(message = "Operating hours cannot be blank")
    @Size(max = 100, message = "Operating hours cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String hours;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    @Column(nullable = false)
    private double rating;

    @PositiveOrZero(message = "Number of reviews must be a positive number or zero")
    @Column(name = "review_count", nullable = false) // Changement du nom de colonne
    private int reviewCount; // Renommage du champ

    @OneToMany(mappedBy = "espace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews; // Liste des avis
    
    @ElementCollection
    @CollectionTable(name = "espace_amenities", joinColumns = @JoinColumn(name = "espace_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    @ElementCollection
    @CollectionTable(name = "espace_features", joinColumns = @JoinColumn(name = "espace_id"))
    @Column(name = "feature")
    private List<String> features;
    
    @ElementCollection
    @CollectionTable(name = "espace_gallery", joinColumns = @JoinColumn(name = "espace_id"))
    @Column(name = "image_url")
    private List<String> gallery;

    // Méthode pratique pour ajouter un review
    public void addReview(Review review) {
        reviews.add(review);
        review.setEspace(this);
    }

    // Méthode pratique pour supprimer un review
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setEspace(null);
    }

    // Getters & Setters 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }
}