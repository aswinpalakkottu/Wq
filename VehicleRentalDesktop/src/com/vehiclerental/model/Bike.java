// src/com/vehiclerental/model/Bike.java
package com.vehiclerental.model;

public class Bike extends Vehicle {
    private int engineCapacity;
    
    public Bike(String vehicleId, String model, double dailyRate, int engineCapacity) {
        super(vehicleId, model, dailyRate);
        this.engineCapacity = engineCapacity;
    }
    
    @Override
    public String getVehicleType() {
        return "Bike";
    }
    
    @Override
    public String getDetails() {
        return String.format("BIKE 🏍️ | ID: %s | Model: %s | Engine: %dcc | Rate: $%.2f/day | Available: %s",
                vehicleId, model, engineCapacity, dailyRate, isAvailable ? "Yes" : "No");
    }
    
    public int getEngineCapacity() { return engineCapacity; }
}