package com.coworking.management.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {

    private Long id;
    private Long userId;
    private Long espaceId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String paymentId;
    
    //Getters & Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getEspaceId() {
		return espaceId;
	}
	public void setEspaceId(Long espaceId) {
		this.espaceId = espaceId;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
    
}