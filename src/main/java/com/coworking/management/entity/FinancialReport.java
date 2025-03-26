package com.coworking.management.entity;

import java.util.List;

import lombok.Data;

@Data
public class FinancialReport {
    private double totalRevenue;
    private double subscriptionRevenue;
    private double eventRevenue;
    private double outstandingPayments;
    private List<RevenueByMonth> monthlyTrends;
	public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public double getSubscriptionRevenue() {
		return subscriptionRevenue;
	}
	public void setSubscriptionRevenue(double subscriptionRevenue) {
		this.subscriptionRevenue = subscriptionRevenue;
	}
	public double getEventRevenue() {
		return eventRevenue;
	}
	public void setEventRevenue(double eventRevenue) {
		this.eventRevenue = eventRevenue;
	}
	public double getOutstandingPayments() {
		return outstandingPayments;
	}
	public void setOutstandingPayments(double outstandingPayments) {
		this.outstandingPayments = outstandingPayments;
	}
	public List<RevenueByMonth> getMonthlyTrends() {
		return monthlyTrends;
	}
	public void setMonthlyTrends(List<RevenueByMonth> monthlyTrends) {
		this.monthlyTrends = monthlyTrends;
	}
    
    
}

@Data
class RevenueByMonth {
    private String month;
    private double amount;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
    
    
}
