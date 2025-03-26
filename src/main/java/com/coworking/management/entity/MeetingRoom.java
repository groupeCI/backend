package com.coworking.management.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MeetingRoom extends Espace {
    private int maxParticipants;
    private boolean hasProjector;
    private boolean hasWhiteboard;
    private boolean hasVideoConference;
    
    @ElementCollection
    private List<String> equipmentList;
    
    @OneToMany(mappedBy = "meetingRoom")
    private List<RecurringReservation> recurringReservations;

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public boolean isHasProjector() {
		return hasProjector;
	}

	public void setHasProjector(boolean hasProjector) {
		this.hasProjector = hasProjector;
	}

	public boolean isHasWhiteboard() {
		return hasWhiteboard;
	}

	public void setHasWhiteboard(boolean hasWhiteboard) {
		this.hasWhiteboard = hasWhiteboard;
	}

	public boolean isHasVideoConference() {
		return hasVideoConference;
	}

	public void setHasVideoConference(boolean hasVideoConference) {
		this.hasVideoConference = hasVideoConference;
	}

	public List<String> getEquipmentList() {
		return equipmentList;
	}

	public void setEquipmentList(List<String> equipmentList) {
		this.equipmentList = equipmentList;
	}

	public List<RecurringReservation> getRecurringReservations() {
		return recurringReservations;
	}

	public void setRecurringReservations(List<RecurringReservation> recurringReservations) {
		this.recurringReservations = recurringReservations;
	}
    
    
}
