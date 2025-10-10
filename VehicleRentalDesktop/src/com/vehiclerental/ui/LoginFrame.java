// LoginFrame.java
package com.vehiclerental.ui;

import com.vehiclerental.model.Admin;
import com.vehiclerental.model.Customer;
import com.vehiclerental.service.RentalService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private RentalService rentalService;
    private JTabbedPane tabbedPane;
    
    // Customer Login Components
    private JTextField customerEmailField;
    private JPasswordField customerPasswordField;
    private JButton customerLoginBtn;
    private JButton customerRegisterBtn;
    
    // Admin Login Components
    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;
    private JButton adminLoginBtn;
    
    public LoginFrame() {
        rentalService = new RentalService();
        initializeService();
        initializeUI();
    }
    
    private void initializeService() {
        // Add sample data
        try {
            rentalService.initializeSampleData();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null,
                "Failed to connect to database. Please ensure MySQL is running and configured correctly.\n" + e.getMessage(),
                "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void initializeUI() {
        setTitle("Cerero Vehicle Rental System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Customer Login Panel
        JPanel customerPanel = createCustomerLoginPanel();
        tabbedPane.addTab("Customer Login", customerPanel);
        
        // Admin Login Panel
        JPanel adminPanel = createAdminLoginPanel();
        tabbedPane.addTab("Admin Login", adminPanel);
        
        add(tabbedPane);
    }
    
    private JPanel createCustomerLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Customer Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        customerEmailField = new JTextField(20);
        panel.add(customerEmailField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        customerPasswordField = new JPasswordField(20);
        panel.add(customerPasswordField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        customerLoginBtn = new JButton("Login");
        customerLoginBtn.addActionListener(new CustomerLoginListener());
        buttonPanel.add(customerLoginBtn);
        
        customerRegisterBtn = new JButton("Register");
        customerRegisterBtn.addActionListener(new CustomerRegisterListener());
        buttonPanel.add(customerRegisterBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createAdminLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Admin Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        adminUsernameField = new JTextField(20);
        panel.add(adminUsernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        adminPasswordField = new JPasswordField(20);
        panel.add(adminPasswordField, gbc);
        
        // Login Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        adminLoginBtn = new JButton("Login");
        adminLoginBtn.addActionListener(new AdminLoginListener());
        panel.add(adminLoginBtn, gbc);
        
        return panel;
    }
    
    private class CustomerLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = customerEmailField.getText().trim();
            String password = new String(customerPasswordField.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter both email and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Customer customer = rentalService.authenticateCustomer(email, password);
            if (customer != null) {
                new CustomerDashboardFrame(rentalService, customer).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class CustomerRegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CustomerRegistrationFrame(rentalService, LoginFrame.this).setVisible(true);
        }
    }
    
    private class AdminLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = adminUsernameField.getText().trim();
            String password = new String(adminPasswordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (rentalService.authenticateAdmin(username, password)) {
                new AdminDashboardFrame(rentalService).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}