// VehicleDAO.java (MySQL Version)
package com.vehiclerental.dao;

import com.vehiclerental.model.*;
import com.vehiclerental.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (vehicle_id, vehicle_type, model, daily_rate, " +
                    "seating_capacity, car_type, engine_capacity, has_storage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicle.getVehicleId());
            stmt.setString(2, vehicle.getVehicleType().toUpperCase());
            stmt.setString(3, vehicle.getModel());
            stmt.setDouble(4, vehicle.getDailyRate());
            
            // Set type-specific parameters
            if (vehicle instanceof Car) {
                Car car = (Car) vehicle;
                stmt.setInt(5, car.getSeatingCapacity());
                stmt.setString(6, car.getCarType());
                stmt.setNull(7, Types.INTEGER);
                stmt.setNull(8, Types.BOOLEAN);
            } else if (vehicle instanceof Bike) {
                Bike bike = (Bike) vehicle;
                stmt.setNull(5, Types.INTEGER);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setInt(7, bike.getEngineCapacity());
                stmt.setNull(8, Types.BOOLEAN);
            } else if (vehicle instanceof Scooter) {
                Scooter scooter = (Scooter) vehicle;
                stmt.setNull(5, Types.INTEGER);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.INTEGER);
                stmt.setBoolean(8, scooter.hasStorage());
            }
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removeVehicle(String vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicleId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Vehicle getVehicle(String vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return resultSetToVehicle(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_type, model";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Vehicle vehicle = resultSetToVehicle(rs);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    
    public boolean updateVehicleAvailability(String vehicleId, boolean available) {
        String sql = "UPDATE vehicles SET is_available = ? WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, available);
            stmt.setString(2, vehicleId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Vehicle resultSetToVehicle(ResultSet rs) throws SQLException {
        String vehicleId = rs.getString("vehicle_id");
        String vehicleType = rs.getString("vehicle_type");
        String model = rs.getString("model");
        double dailyRate = rs.getDouble("daily_rate");
        boolean isAvailable = rs.getBoolean("is_available");
        
        switch (vehicleType) {
            case "CAR":
                int seatingCapacity = rs.getInt("seating_capacity");
                String carType = rs.getString("car_type");
                Car car = new Car(vehicleId, model, dailyRate, seatingCapacity, carType);
                car.setAvailable(isAvailable);
                return car;
                
            case "BIKE":
                int engineCapacity = rs.getInt("engine_capacity");
                Bike bike = new Bike(vehicleId, model, dailyRate, engineCapacity);
                bike.setAvailable(isAvailable);
                return bike;
                
            case "SCOOTER":
                boolean hasStorage = rs.getBoolean("has_storage");
                Scooter scooter = new Scooter(vehicleId, model, dailyRate, hasStorage);
                scooter.setAvailable(isAvailable);
                return scooter;
                
            default:
                return null;
        }
    }
}