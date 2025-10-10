// RentalService.java (Updated for MySQL)
package com.vehiclerental.service;

import com.vehiclerental.model.*;
import com.vehiclerental.dao.*;
import com.vehiclerental.util.DBConnection;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RentalService {
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private RentalDAO rentalDAO = new RentalDAO();
    private AdminDAO adminDAO = new AdminDAO();
    
    public void initializeSampleData() {
        // Check database connection first
        if (!DBConnection.testConnection()) {
            System.err.println("Cannot connect to database! Please check your MySQL configuration.");
            return;
        }
        
        // Add sample admin (this will only insert if not exists due to IGNORE)
        addAdmin(new Admin("admin", "admin123"));
        
        // Check if sample customer exists, if not add them
        if (getCustomerByEmail("john@email.com") == null) {
            registerCustomer(new Customer("John Doe", "john@email.com", "1234567890", "LIC123456", "password123"));
        }
        
        // Vehicles are already inserted by the SQL script
        System.out.println("Sample data initialized successfully!");
    }
    
    public Customer getCustomerByEmail(String email) {
        return customerDAO.getCustomerByEmail(email);
    }
    
    // All other service methods remain the same as they just delegate to DAOs
    public boolean registerCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }
    
    public Customer authenticateCustomer(String email, String password) {
        Customer customer = customerDAO.getCustomerByEmail(email);
        if (customer != null && customer.authenticate(password)) {
            return customer;
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    public boolean addVehicle(Vehicle vehicle) {
        return vehicleDAO.addVehicle(vehicle);
    }
    
    public boolean removeVehicle(String vehicleId) {
        Vehicle vehicle = vehicleDAO.getVehicle(vehicleId);
        if (vehicle != null && vehicle.isAvailable()) {
            return vehicleDAO.removeVehicle(vehicleId);
        }
        return false;
    }
    
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.getAllVehicles();
    }
    
    public List<Vehicle> getAvailableVehicles() {
        return vehicleDAO.getAllVehicles().stream()
                .filter(Vehicle::isAvailable)
                .collect(Collectors.toList());
    }
    
    public Vehicle getVehicle(String vehicleId) {
        return vehicleDAO.getVehicle(vehicleId);
    }
    
    public Rental rentVehicle(Customer customer, String vehicleId, String startDateStr, String endDateStr) {
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
                return null;
            }
            
            Vehicle vehicle = vehicleDAO.getVehicle(vehicleId);
            if (vehicle == null || !vehicle.isAvailable()) {
                return null;
            }
            
            Rental rental = new Rental(customer, vehicle, startDate, endDate);
            if (rentalDAO.addRental(rental)) {
                return rental;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Bill returnVehicle(String rentalId) {
        Rental rental = rentalDAO.getRental(rentalId);
        if (rental != null && "ACTIVE".equals(rental.getStatus())) {
            rental.completeRental(LocalDate.now());
            if (rentalDAO.updateRental(rental)) {
                Bill bill = new Bill(rental);
                return bill;
            }
        }
        return null;
    }
    
    public List<Rental> getCustomerRentalHistory(Customer customer) {
        return rentalDAO.getRentalsByCustomer(customer.getCustomerId());
    }
    
    public List<Rental> getActiveRentals(Customer customer) {
        return rentalDAO.getRentalsByCustomer(customer.getCustomerId()).stream()
                .filter(rental -> "ACTIVE".equals(rental.getStatus()))
                .collect(Collectors.toList());
    }
    
    public List<Rental> getAllRentals() {
        return rentalDAO.getAllRentals();
    }
    
    public void addAdmin(Admin admin) {
        adminDAO.addAdmin(admin);
    }
    
    public boolean authenticateAdmin(String username, String password) {
        Admin admin = adminDAO.getAdmin(username);
        return admin != null && admin.authenticate(username, password);
    }
}