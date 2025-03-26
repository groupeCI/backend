package com.coworking.management.dto;

import lombok.Data;

@Data
public class EventRegistrationDTO {
    private Long eventId;
    private Long userId;
    private boolean applyDiscount; // Si l'utilisateur veut appliquer son discount d'abonnement
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean isApplyDiscount() {
		return applyDiscount;
	}
	public void setApplyDiscount(boolean applyDiscount) {
		this.applyDiscount = applyDiscount;
	}
    
    
}