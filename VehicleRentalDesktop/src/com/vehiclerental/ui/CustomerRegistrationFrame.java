// CustomerRegistrationFrame.java
package com.vehiclerental.ui;

import com.vehiclerental.model.Customer;
import com.vehiclerental.service.RentalService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerRegistrationFrame extends JFrame {
    private RentalService rentalService;
    private JFrame parentFrame;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField licenseField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerBtn;
    private JButton cancelBtn;

    public CustomerRegistrationFrame(RentalService rentalService, JFrame parentFrame) {
        this.rentalService = rentalService;
        this.parentFrame = parentFrame;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Customer Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Register New Customer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Name
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        nameField = new JTextField(20);
        mainPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        phoneField = new JTextField(20);
        mainPanel.add(phoneField, gbc);

        // License Number
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("License Number:"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        licenseField = new JTextField(20);
        mainPanel.add(licenseField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        confirmPasswordField = new JPasswordField(20);
        mainPanel.add(confirmPasswordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());

        registerBtn = new JButton("Register");
        registerBtn.addActionListener(new RegisterListener());
        buttonPanel.add(registerBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String license = licenseField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || license.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                    "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                    "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = new Customer(name, email, phone, license, password);
            if (rentalService.registerCustomer(customer)) {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                    "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                    "Registration failed. Email may already be in use.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
