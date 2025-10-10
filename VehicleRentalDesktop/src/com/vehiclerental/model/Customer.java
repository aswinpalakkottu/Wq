// Customer.java (Updated)
package com.vehiclerental.model;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private String password;
    
    public Customer(String name, String email, String phone, String licenseNumber, String password) {
        this.customerId = "CUST" + System.currentTimeMillis();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.password = password;
    }
    
    // For database reconstruction
    public Customer(String customerId, String name, String email, String phone, String licenseNumber, String password) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.password = password;
    }
    
    // Getters and other methods remain the same...
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getPassword() { return password; }
    
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}