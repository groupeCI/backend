package com.coworking.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ex: Mensuel, Trimestriel, Annuel

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int duration; // Durée en jours

    @Column
    private double discount; // Réduction en pourcentage
    
    private boolean autoRenew;
    private LocalDateTime cancellationDate;
    private String cancellationReason;
    
    @Column(nullable = false)
    private LocalDateTime expirationDate; // Date d'expiration de l'abonnement

    @OneToMany(mappedBy = "subscription")
    private List<User> users;
    
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Invoice> invoices;
    
    private SubscriptionStatus status;

    //Getters & Setters
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}

	public LocalDateTime getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(LocalDateTime cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public SubscriptionStatus getStatus() {
		return status;
	}

	public void setStatus(SubscriptionStatus status) {
		this.status = status;
	}
	
	
	
}