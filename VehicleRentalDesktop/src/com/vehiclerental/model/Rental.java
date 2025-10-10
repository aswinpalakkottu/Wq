// Rental.java (Updated)
package com.vehiclerental.model;

import java.time.LocalDate;

public class Rental {
    private String rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private double totalAmount;
    private String status;
    
    public Rental(Customer customer, Vehicle vehicle, LocalDate startDate, LocalDate endDate) {
        this.rentalId = "RENT" + System.currentTimeMillis();
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "ACTIVE";
        calculateTotalAmount();
    }
    
    // For database reconstruction
    public Rental(String rentalId, Customer customer, Vehicle vehicle, LocalDate startDate, 
                 LocalDate endDate, double totalAmount, String status) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }
    
    // Add setter methods for database operations
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    // Rest of the methods remain the same...
    private void calculateTotalAmount() {
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        this.totalAmount = vehicle.calculateRentalCost((int) days);
    }
    
    public void completeRental(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        this.status = "COMPLETED";
        
        if (actualReturnDate.isAfter(endDate)) {
            long extraDays = java.time.temporal.ChronoUnit.DAYS.between(endDate, actualReturnDate);
            double lateFee = vehicle.getDailyRate() * extraDays * 1.5;
            this.totalAmount += lateFee;
        }
    }
    
    // Getters
    public String getRentalId() { return rentalId; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}