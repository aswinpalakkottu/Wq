// AdminDashboardFrame.java
package com.vehiclerental.ui;

import com.vehiclerental.model.*;
import com.vehiclerental.service.RentalService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    private RentalService rentalService;
    
    private JTabbedPane tabbedPane;
    private JTable vehiclesTable;
    private JTable customersTable;
    private JTable rentalsTable;
    
    public AdminDashboardFrame(RentalService rentalService) {
        this.rentalService = rentalService;
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Cerero Rental - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(70, 130, 180));
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        
        // Vehicles Management Tab
        JPanel vehiclesPanel = createVehiclesPanel();
        tabbedPane.addTab("Manage Vehicles", vehiclesPanel);
        
        // Customers Tab
        JPanel customersPanel = createCustomersPanel();
        tabbedPane.addTab("Customers", customersPanel);
        
        // Rentals Tab
        JPanel rentalsPanel = createRentalsPanel();
        tabbedPane.addTab("All Rentals", rentalsPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table for vehicles
        String[] columns = {"Vehicle ID", "Type", "Model", "Details", "Daily Rate", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        vehiclesTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(vehiclesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadVehicles());
        buttonPanel.add(refreshBtn);
        
        JButton addBtn = new JButton("Add Vehicle");
        addBtn.addActionListener(new AddVehicleListener());
        buttonPanel.add(addBtn);
        
        JButton removeBtn = new JButton("Remove Selected Vehicle");
        removeBtn.addActionListener(new RemoveVehicleListener());
        buttonPanel.add(removeBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table for customers
        String[] columns = {"Customer ID", "Name", "Email", "Phone", "License Number"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customersTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(customersTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadCustomers());
        buttonPanel.add(refreshBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRentalsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table for rentals
        String[] columns = {"Rental ID", "Customer", "Vehicle", "Start Date", "End Date", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        rentalsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(rentalsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadRentals());
        buttonPanel.add(refreshBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void loadData() {
        loadVehicles();
        loadCustomers();
        loadRentals();
    }
    
    public void loadVehicles() {
        DefaultTableModel model = (DefaultTableModel) vehiclesTable.getModel();
        model.setRowCount(0);
        
        List<Vehicle> vehicles = rentalService.getAllVehicles();
        for (Vehicle vehicle : vehicles) {
            model.addRow(new Object[]{
                vehicle.getVehicleId(),
                vehicle.getVehicleType(),
                vehicle.getModel(),
                getVehicleDetails(vehicle),
                String.format("$%.2f", vehicle.getDailyRate()),
                vehicle.isAvailable() ? "Available" : "Rented"
            });
        }
    }
    
    public void loadCustomers() {
        DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
        model.setRowCount(0);
        
        List<Customer> customers = rentalService.getAllCustomers();
        for (Customer customer : customers) {
            model.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getLicenseNumber()
            });
        }
    }
    
    public void loadRentals() {
        DefaultTableModel model = (DefaultTableModel) rentalsTable.getModel();
        model.setRowCount(0);
        
        List<Rental> rentals = rentalService.getAllRentals();
        for (Rental rental : rentals) {
            model.addRow(new Object[]{
                rental.getRentalId(),
                rental.getCustomer().getName(),
                rental.getVehicle().getModel(),
                rental.getStartDate(),
                rental.getEndDate(),
                String.format("$%.2f", rental.getTotalAmount()),
                rental.getStatus()
            });
        }
    }
    
    private String getVehicleDetails(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            return car.getCarType() + ", " + car.getSeatingCapacity() + " seats";
        } else if (vehicle instanceof Bike) {
            Bike bike = (Bike) vehicle;
            return bike.getEngineCapacity() + "cc";
        } else if (vehicle instanceof Scooter) {
            Scooter scooter = (Scooter) vehicle;
            return "Storage: " + (scooter.hasStorage() ? "Yes" : "No");
        }
        return "";
    }
    
    private class AddVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AddVehicleDialog(AdminDashboardFrame.this, rentalService).setVisible(true);
        }
    }
    
    private class RemoveVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = vehiclesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AdminDashboardFrame.this,
                    "Please select a vehicle to remove", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String vehicleId = (String) vehiclesTable.getValueAt(selectedRow, 0);
            String status = (String) vehiclesTable.getValueAt(selectedRow, 5);
            
            if ("Rented".equals(status)) {
                JOptionPane.showMessageDialog(AdminDashboardFrame.this,
                    "Cannot remove a rented vehicle", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(AdminDashboardFrame.this,
                "Are you sure you want to remove this vehicle?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (rentalService.removeVehicle(vehicleId)) {
                    JOptionPane.showMessageDialog(AdminDashboardFrame.this,
                        "Vehicle removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadVehicles();
                } else {
                    JOptionPane.showMessageDialog(AdminDashboardFrame.this,
                        "Failed to remove vehicle", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}