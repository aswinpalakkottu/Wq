// RentalDAO.java (MySQL Version)
package com.vehiclerental.dao;

import com.vehiclerental.model.*;
import com.vehiclerental.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    
    public boolean addRental(Rental rental) {
        String sql = "INSERT INTO rentals (rental_id, customer_id, vehicle_id, start_date, end_date, total_amount, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, rental.getRentalId());
            stmt.setString(2, rental.getCustomer().getCustomerId());
            stmt.setString(3, rental.getVehicle().getVehicleId());
            stmt.setDate(4, Date.valueOf(rental.getStartDate()));
            stmt.setDate(5, Date.valueOf(rental.getEndDate()));
            stmt.setDouble(6, rental.getTotalAmount());
            stmt.setString(7, rental.getStatus());
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success) {
                // Update vehicle availability
                vehicleDAO.updateVehicleAvailability(rental.getVehicle().getVehicleId(), false);
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Rental getRental(String rentalId) {
        String sql = "SELECT r.*, c.*, v.* FROM rentals r " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "JOIN vehicles v ON r.vehicle_id = v.vehicle_id " +
                    "WHERE r.rental_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, rentalId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return resultSetToRental(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Rental> getRentalsByCustomer(String customerId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.*, c.*, v.* FROM rentals r " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "JOIN vehicles v ON r.vehicle_id = v.vehicle_id " +
                    "WHERE r.customer_id = ? ORDER BY r.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rentals.add(resultSetToRental(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }
    
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.*, c.*, v.* FROM rentals r " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "JOIN vehicles v ON r.vehicle_id = v.vehicle_id " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rentals.add(resultSetToRental(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }
    
    public boolean updateRental(Rental rental) {
        String sql = "UPDATE rentals SET actual_return_date = ?, status = ?, total_amount = ? " +
                    "WHERE rental_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, rental.getActualReturnDate() != null ? 
                Date.valueOf(rental.getActualReturnDate()) : null);
            stmt.setString(2, rental.getStatus());
            stmt.setDouble(3, rental.getTotalAmount());
            stmt.setString(4, rental.getRentalId());
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success && "COMPLETED".equals(rental.getStatus())) {
                // Update vehicle availability when rental is completed
                vehicleDAO.updateVehicleAvailability(rental.getVehicle().getVehicleId(), true);
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Rental resultSetToRental(ResultSet rs) throws SQLException {
        // Create Customer
        Customer customer = customerDAO.resultSetToCustomer(rs);
        
        // Create Vehicle
        Vehicle vehicle = vehicleDAO.resultSetToVehicle(rs);
        
        // Create Rental
        String rentalId = rs.getString("rental_id");
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();
        
        Rental rental = new Rental(customer, vehicle, startDate, endDate) {
            @Override
            public String getRentalId() {
                return rentalId;
            }
        };
        
        // Set additional rental properties
        Date actualReturnDate = rs.getDate("actual_return_date");
        if (actualReturnDate != null) {
            rental.completeRental(actualReturnDate.toLocalDate());
        } else {
            rental.setStatus(rs.getString("status"));
            rental.setTotalAmount(rs.getDouble("total_amount"));
        }
        
        return rental;
    }
}