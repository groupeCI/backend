package com.coworking.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.coworking.management.entity.Espace;
import com.coworking.management.entity.Event;
import com.coworking.management.entity.RoleEnum;
import com.coworking.management.entity.SpecialPrice;
import com.coworking.management.entity.Subscription;
import com.coworking.management.entity.User;
import lombok.Data;
import java.util.List;
import java.util.Map;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    
    //User
    private String email;
    private String password;
    private RoleEnum role;
    private User users;
    private List<User> usersList;
    
    private List<Map<String, Object>> profilesList;
    private Map<String, Object> profile;
    
    //Espace
    private Espace espaces;
    private List<Espace> espacesList;
    
    //Subscription
    private Subscription subscription;
    private List<Subscription> SubscriptionsList;
    
    //Event
    private Event event;
    private List<Event> EventsList;
  
    //Review
    private ReviewResponseDTO review;
    private List<ReviewResponseDTO> ReviewsList;
    
    //Pricing
    private SpecialPrice specialPrice;
    private List<SpecialPrice> specialPricesList;
    
   //Getters & Setters 
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public RoleEnum getRole() {
		return role;
	}
	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public User getUsers() {
		return users;
	}
	public void setUsers(User users) {
		this.users = users;
	}
	public List<User> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}
	
	public Espace getEspaces() {
		return espaces;
	}
	public void setEspaces(Espace espaces) {
		this.espaces = espaces;
	}
	public List<Espace> getEspacesList() {
		return espacesList;
	}
	public void setEspacesList(List<Espace> espacesList) {
		this.espacesList = espacesList;
	}
	
	public Subscription getSubscription() {
		return subscription;
	}
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	public List<Subscription> getSubscriptionsList() {
		return SubscriptionsList;
	}
	public void setSubscriptionsList(List<Subscription> subscriptionsList) {
		SubscriptionsList = subscriptionsList;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public List<Event> getEventsList() {
		return EventsList;
	}
	public void setEventsList(List<Event> eventsList) {
		EventsList = eventsList;
	}
	public List<Map<String, Object>> getProfilesList() {
		return profilesList;
	}
	public void setProfilesList(List<Map<String, Object>> profilesList) {
		this.profilesList = profilesList;
	}
	public Map<String, Object> getProfile() {
		return profile;
	}
	public void setProfile(Map<String, Object> profile) {
		this.profile = profile;
	}
	public ReviewResponseDTO getReview() {
		return review;
	}
	public void setReview(ReviewResponseDTO review) {
		this.review = review;
	}
	public List<ReviewResponseDTO> getReviewsList() {
		return ReviewsList;
	}
	public void setReviewsList(List<ReviewResponseDTO> reviewsList) {
		ReviewsList = reviewsList;
	}
	public SpecialPrice getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(SpecialPrice specialPrice) {
		this.specialPrice = specialPrice;
	}
	public List<SpecialPrice> getSpecialPricesList() {
		return specialPricesList;
	}
	public void setSpecialPricesList(List<SpecialPrice> specialPricesList) {
		this.specialPricesList = specialPricesList;
	}
	
	
}