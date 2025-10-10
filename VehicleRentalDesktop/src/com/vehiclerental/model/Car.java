// src/com/vehiclerental/model/Car.java
package com.vehiclerental.model;

public class Car extends Vehicle {
    private int seatingCapacity;
    private String carType;
    
    public Car(String vehicleId, String model, double dailyRate, int seatingCapacity, String carType) {
        super(vehicleId, model, dailyRate);
        this.seatingCapacity = seatingCapacity;
        this.carType = carType;
    }
    
    @Override
    public String getVehicleType() {
        return "Car";
    }
    
    @Override
    public String getDetails() {
        return String.format("CAR 🚗 | ID: %s | Model: %s | Type: %s | Seats: %d | Rate: $%.2f/day | Available: %s",
                vehicleId, model, carType, seatingCapacity, dailyRate, isAvailable ? "Yes" : "No");
    }
    
    // Encapsulation
    public int getSeatingCapacity() { return seatingCapacity; }
    public String getCarType() { return carType; }
}