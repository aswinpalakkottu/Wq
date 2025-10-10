// CustomerDashboardFrame.java
package com.vehiclerental.ui;

import com.vehiclerental.model.*;
import com.vehiclerental.service.RentalService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerDashboardFrame extends JFrame {
    private RentalService rentalService;
    private Customer customer;
    
    private JTabbedPane tabbedPane;
    private JTable vehiclesTable;
    private JTable rentalsTable;
    private JLabel welcomeLabel;
    
    public CustomerDashboardFrame(RentalService rentalService, Customer customer) {
        this.rentalService = rentalService;
        this.customer = customer;
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Cerero Rental - Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(70, 130, 180));
        
        welcomeLabel = new JLabel("Welcome, " + customer.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        
        // Browse Vehicles Tab
        JPanel browsePanel = createBrowsePanel();
        tabbedPane.addTab("Browse Vehicles", browsePanel);
        
        // My Rentals Tab
        JPanel rentalsPanel = createRentalsPanel();
        tabbedPane.addTab("My Rentals", rentalsPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createBrowsePanel() {
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
        
        JButton rentBtn = new JButton("Rent Selected Vehicle");
        rentBtn.addActionListener(new RentVehicleListener());
        buttonPanel.add(rentBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRentalsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table for rentals
        String[] columns = {"Rental ID", "Vehicle", "Start Date", "End Date", "Total Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        rentalsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(rentalsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadRentals());
        buttonPanel.add(refreshBtn);
        
        JButton returnBtn = new JButton("Return Selected Vehicle");
        returnBtn.addActionListener(new ReturnVehicleListener());
        buttonPanel.add(returnBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void loadData() {
        loadVehicles();
        loadRentals();
    }
    
    public void loadVehicles() {
        DefaultTableModel model = (DefaultTableModel) vehiclesTable.getModel();
        model.setRowCount(0);
        
        List<Vehicle> vehicles = rentalService.getAvailableVehicles();
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
    
    private void loadRentals() {
        DefaultTableModel model = (DefaultTableModel) rentalsTable.getModel();
        model.setRowCount(0);
        
        List<Rental> rentals = rentalService.getCustomerRentalHistory(customer);
        for (Rental rental : rentals) {
            model.addRow(new Object[]{
                rental.getRentalId(),
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
    
    private class RentVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = vehiclesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerDashboardFrame.this,
                    "Please select a vehicle to rent", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String vehicleId = (String) vehiclesTable.getValueAt(selectedRow, 0);
            new RentVehicleDialog(CustomerDashboardFrame.this, rentalService, customer, vehicleId).setVisible(true);
        }
    }
    
    private class ReturnVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = rentalsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerDashboardFrame.this,
                    "Please select a rental to return", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String rentalId = (String) rentalsTable.getValueAt(selectedRow, 0);
            String status = (String) rentalsTable.getValueAt(selectedRow, 5);
            
            if (!"ACTIVE".equals(status)) {
                JOptionPane.showMessageDialog(CustomerDashboardFrame.this,
                    "Only active rentals can be returned", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Bill bill = rentalService.returnVehicle(rentalId);
            if (bill != null) {
                JOptionPane.showMessageDialog(CustomerDashboardFrame.this,
                    bill.generateBill(), "Return Successful", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(CustomerDashboardFrame.this,
                    "Failed to return vehicle", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}