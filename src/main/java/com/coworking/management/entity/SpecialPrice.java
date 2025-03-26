package com.coworking.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class SpecialPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    
    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // PERCENTAGE, FIXED_AMOUNT
    
    @ManyToOne
    private Espace espace; // Null si applicable à tous les espaces
    
    @ManyToOne
    private Subscription subscription; // Null si applicable à tous les abonnements
    
    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public Espace getEspace() {
		return espace;
	}

	public void setEspace(Espace espace) {
		this.espace = espace;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
    
    
    
}