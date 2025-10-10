-- database/schema.sql
CREATE DATABASE IF NOT EXISTS vehicle_rental_system;
USE vehicle_rental_system;

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    license_number VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admins table
CREATE TABLE IF NOT EXISTS admins (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id VARCHAR(50) PRIMARY KEY,
    vehicle_type ENUM('CAR', 'BIKE', 'SCOOTER') NOT NULL,
    model VARCHAR(100) NOT NULL,
    daily_rate DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    
    -- Car specific fields
    seating_capacity INT,
    car_type VARCHAR(50),
    
    -- Bike specific fields
    engine_capacity INT,
    
    -- Scooter specific fields
    has_storage BOOLEAN,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rentals table
CREATE TABLE IF NOT EXISTS rentals (
    rental_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    vehicle_id VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    actual_return_date DATE,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

-- Insert sample data
INSERT IGNORE INTO admins (username, password) VALUES ('admin', 'admin123');

INSERT IGNORE INTO customers (customer_id, name, email, phone, license_number, password) 
VALUES ('CUST001', 'John Doe', 'john@email.com', '1234567890', 'LIC123456', 'password123');

INSERT IGNORE INTO vehicles (vehicle_id, vehicle_type, model, daily_rate, seating_capacity, car_type) 
VALUES 
('V001', 'CAR', 'Toyota Camry', 50.00, 5, 'Sedan'),
('V002', 'CAR', 'Honda Civic', 45.00, 5, 'Sedan'),
('V003', 'CAR', 'Ford Mustang', 80.00, 4, 'Sports');

INSERT IGNORE INTO vehicles (vehicle_id, vehicle_type, model, daily_rate, engine_capacity) 
VALUES 
('V004', 'BIKE', 'Yamaha MT-15', 25.00, 150),
('V005', 'BIKE', 'Royal Enfield Classic', 35.00, 350);

INSERT IGNORE INTO vehicles (vehicle_id, vehicle_type, model, daily_rate, has_storage) 
VALUES 
('V006', 'SCOOTER', 'Honda Activa', 15.00, TRUE),
('V007', 'SCOOTER', 'TVS Jupiter', 18.00, FALSE);