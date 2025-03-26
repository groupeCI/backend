package com.coworking.management.dto;

import lombok.Data;

@Data
public class RecurringReservationRequest {
    private ReservationRequestDto reservationRequest;
    private int occurrences;
	public ReservationRequestDto getReservationRequest() {
		return reservationRequest;
	}
	public void setReservationRequest(ReservationRequestDto reservationRequest) {
		this.reservationRequest = reservationRequest;
	}
	public int getOccurrences() {
		return occurrences;
	}
	public void setOccurrences(int occurrences) {
		this.occurrences = occurrences;
	}
    
    
}