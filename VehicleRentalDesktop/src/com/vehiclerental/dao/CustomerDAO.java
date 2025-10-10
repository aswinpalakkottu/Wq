// CustomerDAO.java (MySQL Version)
package com.vehiclerental.dao;

import com.vehiclerental.model.Customer;
import com.vehiclerental.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, name, email, phone, license_number, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getLicenseNumber());
            stmt.setString(6, customer.getPassword());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Customer getCustomer(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return resultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return resultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(resultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    public Customer resultSetToCustomer(ResultSet rs) throws SQLException {
        String customerId = rs.getString("customer_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String licenseNumber = rs.getString("license_number");
        String password = rs.getString("password");
        
        return new Customer(name, email, phone, licenseNumber, password) {
            @Override
            public String getCustomerId() {
                return customerId;
            }
        };
    }
}