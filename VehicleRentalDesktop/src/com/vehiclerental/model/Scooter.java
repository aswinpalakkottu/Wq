// src/com/vehiclerental/model/Scooter.java
package com.vehiclerental.model;

public class Scooter extends Vehicle {
    private boolean hasStorage;
    
    public Scooter(String vehicleId, String model, double dailyRate, boolean hasStorage) {
        super(vehicleId, model, dailyRate);
        this.hasStorage = hasStorage;
    }
    
    @Override
    public String getVehicleType() {
        return "Scooter";
    }
    
    @Override
    public String getDetails() {
        return String.format("SCOOTER 🛵 | ID: %s | Model: %s | Storage: %s | Rate: $%.2f/day | Available: %s",
                vehicleId, model, hasStorage ? "Yes" : "No", dailyRate, isAvailable ? "Yes" : "No");
    }
    
    public boolean hasStorage() { return hasStorage; }
}