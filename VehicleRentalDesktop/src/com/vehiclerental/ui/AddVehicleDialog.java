// AddVehicleDialog.java
package com.vehiclerental.ui;

import com.vehiclerental.model.*;
import com.vehiclerental.service.RentalService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AddVehicleDialog extends JDialog {
    private RentalService rentalService;
    private JFrame parentFrame;
    
    private JComboBox<String> typeComboBox;
    private JTextField vehicleIdField;
    private JTextField modelField;
    private JTextField dailyRateField;
    
    // Car specific fields
    private JTextField seatingCapacityField;
    private JTextField carTypeField;
    
    // Bike specific fields
    private JTextField engineCapacityField;
    
    // Scooter specific fields
    private JCheckBox hasStorageCheckBox;
    
    private JPanel specificFieldsPanel;
    private JButton addBtn;
    private JButton cancelBtn;
    
    public AddVehicleDialog(JFrame parent, RentalService rentalService) {
        super(parent, "Add New Vehicle", true);
        this.parentFrame = parent;
        this.rentalService = rentalService;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(400, 400);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Vehicle Type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Vehicle Type:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        typeComboBox = new JComboBox<>(new String[]{"Car", "Bike", "Scooter"});
        typeComboBox.addItemListener(new TypeChangeListener());
        panel.add(typeComboBox, gbc);
        
        // Vehicle ID
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Vehicle ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        vehicleIdField = new JTextField(20);
        panel.add(vehicleIdField, gbc);
        
        // Model
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Model:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        modelField = new JTextField(20);
        panel.add(modelField, gbc);
        
        // Daily Rate
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Daily Rate:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        dailyRateField = new JTextField(20);
        panel.add(dailyRateField, gbc);
        
        // Specific fields panel (initially for Car)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        specificFieldsPanel = createCarFieldsPanel();
        panel.add(specificFieldsPanel, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        addBtn = new JButton("Add Vehicle");
        addBtn.addActionListener(new AddVehicleListener());
        buttonPanel.add(addBtn);
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        panel.add(buttonPanel, gbc);
        
        add(panel);
    }
    
    private JPanel createCarFieldsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        panel.add(new JLabel("Seating Capacity:"));
        seatingCapacityField = new JTextField();
        panel.add(seatingCapacityField);
        
        panel.add(new JLabel("Car Type:"));
        carTypeField = new JTextField();
        panel.add(carTypeField);
        
        return panel;
    }
    
    private JPanel createBikeFieldsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        
        panel.add(new JLabel("Engine Capacity (cc):"));
        engineCapacityField = new JTextField();
        panel.add(engineCapacityField);
        
        return panel;
    }
    
    private JPanel createScooterFieldsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        
        panel.add(new JLabel("Has Storage:"));
        hasStorageCheckBox = new JCheckBox();
        panel.add(hasStorageCheckBox);
        
        return panel;
    }
    
    private class TypeChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                specificFieldsPanel.removeAll();
                
                switch (selectedType) {
                    case "Car":
                        specificFieldsPanel = createCarFieldsPanel();
                        break;
                    case "Bike":
                        specificFieldsPanel = createBikeFieldsPanel();
                        break;
                    case "Scooter":
                        specificFieldsPanel = createScooterFieldsPanel();
                        break;
                }
                
                specificFieldsPanel.revalidate();
                specificFieldsPanel.repaint();
                pack();
            }
        }
    }
    
    private class AddVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String type = (String) typeComboBox.getSelectedItem();
                String vehicleId = vehicleIdField.getText().trim();
                String model = modelField.getText().trim();
                double dailyRate = Double.parseDouble(dailyRateField.getText().trim());
                
                if (vehicleId.isEmpty() || model.isEmpty()) {
                    JOptionPane.showMessageDialog(AddVehicleDialog.this,
                        "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Vehicle vehicle = null;
                
                switch (type) {
                    case "Car":
                        int capacity = Integer.parseInt(seatingCapacityField.getText().trim());
                        String carType = carTypeField.getText().trim();
                        vehicle = new Car(vehicleId, model, dailyRate, capacity, carType);
                        break;
                    case "Bike":
                        int engineCapacity = Integer.parseInt(engineCapacityField.getText().trim());
                        vehicle = new Bike(vehicleId, model, dailyRate, engineCapacity);
                        break;
                    case "Scooter":
                        boolean hasStorage = hasStorageCheckBox.isSelected();
                        vehicle = new Scooter(vehicleId, model, dailyRate, hasStorage);
                        break;
                }
                
                if (rentalService.addVehicle(vehicle)) {
                    JOptionPane.showMessageDialog(AddVehicleDialog.this,
                        "Vehicle added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    
                    // Refresh parent frame
                    if (parentFrame instanceof AdminDashboardFrame) {
                        ((AdminDashboardFrame) parentFrame).loadVehicles();
                    }
                } else {
                    JOptionPane.showMessageDialog(AddVehicleDialog.this,
                        "Failed to add vehicle. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AddVehicleDialog.this,
                    "Please enter valid numbers for numeric fields", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}