// RentVehicleDialog.java
package com.vehiclerental.ui;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;
import com.vehiclerental.util.DateUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class RentVehicleDialog extends JDialog {
    private RentalService rentalService;
    private Customer customer;
    private String vehicleId;
    private JFrame parentFrame;
    
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton rentBtn;
    private JButton cancelBtn;
    
    public RentVehicleDialog(JFrame parent, RentalService rentalService, Customer customer, String vehicleId) {
        super(parent, "Rent Vehicle", true);
        this.parentFrame = parent;
        this.rentalService = rentalService;
        this.customer = customer;
        this.vehicleId = vehicleId;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(400, 200);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Start Date
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Start Date:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        SpinnerDateModel startModel = new SpinnerDateModel();
        startDateSpinner = new JSpinner(startModel);
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        panel.add(startDateSpinner, gbc);
        
        // End Date
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("End Date:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        SpinnerDateModel endModel = new SpinnerDateModel();
        endDateSpinner = new JSpinner(endModel);
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        panel.add(endDateSpinner, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        rentBtn = new JButton("Rent Vehicle");
        rentBtn.addActionListener(new RentListener());
        buttonPanel.add(rentBtn);
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        panel.add(buttonPanel, gbc);
        
        add(panel);
    }
    
    private class RentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                java.util.Date startUtilDate = (java.util.Date) startDateSpinner.getValue();
                java.util.Date endUtilDate = (java.util.Date) endDateSpinner.getValue();
                
                LocalDate startDate = new java.sql.Date(startUtilDate.getTime()).toLocalDate();
                LocalDate endDate = new java.sql.Date(endUtilDate.getTime()).toLocalDate();
                
                if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(RentVehicleDialog.this,
                        "Invalid dates! Start date must be today or later, and end date must be after start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Rental rental = rentalService.rentVehicle(customer, vehicleId, 
                    startDate.toString(), endDate.toString());
                
                if (rental != null) {
                    JOptionPane.showMessageDialog(RentVehicleDialog.this,
                        "Vehicle rented successfully!\nRental ID: " + rental.getRentalId() +
                        "\nTotal Amount: $" + String.format("%.2f", rental.getTotalAmount()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    
                    // Refresh parent frame
                    if (parentFrame instanceof CustomerDashboardFrame) {
                        ((CustomerDashboardFrame) parentFrame).loadData();
                    }
                } else {
                    JOptionPane.showMessageDialog(RentVehicleDialog.this,
                        "Failed to rent vehicle. It might be unavailable.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RentVehicleDialog.this,
                    "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}