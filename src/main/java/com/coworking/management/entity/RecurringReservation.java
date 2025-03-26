package com.coworking.management.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recurring_reservations")
@Data
public class RecurringReservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private LocalDate startDate;
    private LocalDate endDate;
    
    @ManyToOne
    private MeetingRoom meetingRoom;
    
    @ManyToOne
    private User organizer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}
    
    
}
