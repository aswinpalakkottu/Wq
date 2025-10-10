// src/com/vehiclerental/model/Vehicle.java
package com.vehiclerental.model;

public abstract class Vehicle {
    protected String vehicleId;
    protected String model;
    protected double dailyRate;
    protected boolean isAvailable;
    
    public Vehicle(String vehicleId, String model, double dailyRate) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.dailyRate = dailyRate;
        this.isAvailable = true;
    }
    
    // Encapsulation - Getters and Setters
    public String getVehicleId() { return vehicleId; }
    public String getModel() { return model; }
    public double getDailyRate() { return dailyRate; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    // Polymorphism - Abstract method
    public abstract String getVehicleType();
    public abstract String getDetails();
    
    // Polymorphism - Calculate rental cost
    public double calculateRentalCost(int days) {
        return days * dailyRate;
    }
}