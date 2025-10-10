// src/com/vehiclerental/model/Admin.java
package com.vehiclerental.model;

public class Admin {
    private String username;
    private String password;
    
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                '}';
    }
}