// src/com/vehiclerental/model/Bill.java
package com.vehiclerental.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {
    private String billId;
    private Rental rental;
    private LocalDateTime issueDate;
    private double totalAmount;
    
    public Bill(Rental rental) {
        this.billId = "BILL" + System.currentTimeMillis();
        this.rental = rental;
        this.issueDate = LocalDateTime.now();
        this.totalAmount = rental.getTotalAmount();
    }
    
    public String generateBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("=========================================\n");
        bill.append("            CERERO RENTAL BILL           \n");
        bill.append("=========================================\n");
        bill.append("Bill ID: ").append(billId).append("\n");
        bill.append("Issue Date: ").append(issueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        bill.append("Customer: ").append(rental.getCustomer().getName()).append("\n");
        bill.append("Vehicle: ").append(rental.getVehicle().getModel()).append("\n");
        bill.append("Rental Period: ").append(rental.getStartDate()).append(" to ").append(rental.getEndDate()).append("\n");
        if (rental.getActualReturnDate() != null) {
            bill.append("Actual Return: ").append(rental.getActualReturnDate()).append("\n");
        }
        bill.append("Total Amount: $").append(String.format("%.2f", totalAmount)).append("\n");
        bill.append("=========================================\n");
        bill.append("        Thank you for choosing us!       \n");
        bill.append("=========================================\n");
        
        return bill.toString();
    }
    
    // Getters
    public String getBillId() { return billId; }
    public Rental getRental() { return rental; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public double getTotalAmount() { return totalAmount; }
    
    @Override
    public String toString() {
        return "Bill{" +
                "billId='" + billId + '\'' +
                ", rental=" + rental.getRentalId() +
                ", issueDate=" + issueDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}