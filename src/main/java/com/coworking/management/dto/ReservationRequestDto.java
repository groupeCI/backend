package com.coworking.management.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {

    private Long espaceId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    //Getters & Setters
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
    
    
}