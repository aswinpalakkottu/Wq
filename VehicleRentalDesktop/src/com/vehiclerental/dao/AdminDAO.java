// AdminDAO.java (MySQL Version)
package com.vehiclerental.dao;

import com.vehiclerental.model.Admin;
import com.vehiclerental.util.DBConnection;
import java.sql.*;

public class AdminDAO {
    
    public void addAdmin(Admin admin) {
        String sql = "INSERT IGNORE INTO admins (username, password) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Admin getAdmin(String username) {
        String sql = "SELECT * FROM admins WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Admin(
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}