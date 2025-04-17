CREATE DATABASE IF NOT EXISTS CarRentalDB;
use carrentaldb;
show tables;
-- drop database carrentaldb;

CREATE TABLE IF NOT EXISTS Customers (
    customerID INT AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    licenceNumber varchar(30) UNIQUE NOT NULL,
    PRIMARY KEY (customerID)
);

CREATE TABLE IF NOT EXISTS ContactInfo (
    contactID INT AUTO_INCREMENT,
    customerID INT UNIQUE,
    email VARCHAR(100) NOT NULL,-- CHECK VALIDATION IN JAVA
    phoneNumber VARCHAR(15) UNIQUE NOT NULL,
    address VARCHAR(50),
    role ENUM('ADMIN', 'HOST', 'CUSTOMER') DEFAULT 'CUSTOMER',
    PRIMARY KEY (contactID),
    FOREIGN KEY (customerID) REFERENCES Customers(customerID) ON DELETE CASCADE,
    INDEX idx_phone (phoneNumber)
);


CREATE TABLE IF NOT EXISTS Vehicles (
    vehicleID INT AUTO_INCREMENT,
    make VARCHAR(50),
    model VARCHAR(50),
    manufacturingYear INT,
    dailyRate DECIMAL(10,2),
    passengerCapacity INT DEFAULT 5,-- min 5 seat 
    engineCapacity DECIMAL(10,2),
    rating DECIMAL(2,1) DEFAULT 0.0,
    totalRating INT DEFAULT 0.0,
    ratingCount INT DEFAULT 0,
    PRIMARY KEY (vehicleID),
    INDEX idx_make_model (make, model) -- Helps in searching/filtering cars by make & model quickly
);

CREATE TABLE IF NOT EXISTS Auth (
    authID INT AUTO_INCREMENT,
    customerID INT UNIQUE,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL CHECK (CHAR_LENGTH(password) BETWEEN 6 AND 12),
    PRIMARY KEY (authID),
    FOREIGN KEY (customerID) REFERENCES Customers(customerID) ON DELETE CASCADE,
    INDEX username_index (username)
);

CREATE TABLE IF NOT EXISTS Leases (
    leaseID INT AUTO_INCREMENT,
    vehicleID INT,
    customerID INT,
    startDate DATE DEFAULT (CURRENT_DATE),
    endDate DATE,
    leaseType ENUM('Daily', 'Monthly') DEFAULT 'Daily',
    PRIMARY KEY (leaseID),
    FOREIGN KEY (vehicleID) REFERENCES Vehicles(vehicleID) ON DELETE CASCADE,
    FOREIGN KEY (customerId) REFERENCES customers(customerID) ON DELETE CASCADE,
    INDEX idx_start_date (startDate) -- Makes date-based queries (past leases, active rentals) faster
);

CREATE TABLE IF NOT EXISTS CarStatus (
    vehicleStatusID INT AUTO_INCREMENT,
    status ENUM('AVAILABLE', 'NOT_AVAILABLE', 'MAINTENANCE', 'BOOKED') DEFAULT 'NOT_AVAILABLE',
    vehicleID INT UNIQUE,
    PRIMARY KEY (vehicleStatusID),
    FOREIGN KEY (vehicleID) REFERENCES Vehicles(vehicleID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Payments (
    paymentID INT AUTO_INCREMENT,
    leaseID INT UNIQUE,
    paymentDate DATE DEFAULT (CURRENT_DATE),
    amount DECIMAL(10,2) check (amount > 0.0),
    paymentStatus ENUM('PENDING', 'PAID', 'FAILED') DEFAULT 'PENDING',
    PRIMARY KEY (paymentID),
    FOREIGN KEY (leaseID) REFERENCES Leases(leaseID) ON DELETE CASCADE,
    INDEX idx_payment_date (paymentDate) -- Helps in retrieving payment history sorted by date efficiently.
);

CREATE TABLE IF NOT EXISTS City (
    cityID INT AUTO_INCREMENT,
    cityName VARCHAR(100) UNIQUE NOT NULL,
    state VARCHAR(100) NOT NULL,
    PRIMARY KEY (cityID),
    INDEX idx_city_name (cityName) -- speed up city searches
); -- A City can have many Vehicles


CREATE TABLE IF NOT EXISTS HostCustomers (
    hostCustomerID INT AUTO_INCREMENT,
    customerID INT UNIQUE,
    hostingCityID INT NOT NULL,
    gstNumber VARCHAR(50) UNIQUE NOT NULL,
    PRIMARY KEY (hostCustomerID),
    FOREIGN KEY (customerID) REFERENCES Customers(customerID) ON DELETE CASCADE,
    FOREIGN KEY (hostingCityID) REFERENCES City(cityID) ON DELETE CASCADE
);-- a customer can be both a renter and a host

CREATE TABLE IF NOT EXISTS HostVehicle (
    hostVehicleID INT AUTO_INCREMENT,
    hostCustomerID INT,
    vehicleID INT UNIQUE,
    PRIMARY KEY (hostVehicleID),
    FOREIGN KEY (hostCustomerID) REFERENCES HostCustomers(hostCustomerID) ON DELETE CASCADE,
    FOREIGN KEY (vehicleID) REFERENCES Vehicles(vehicleID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS VehicleCity (
    vehicleCityID INT AUTO_INCREMENT,
    vehicleID INT UNIQUE,
    cityID INT,
    PRIMARY KEY (vehicleCityID),
    FOREIGN KEY (vehicleID) REFERENCES Vehicles(vehicleID) ON DELETE CASCADE,
    FOREIGN KEY (cityID) REFERENCES City(cityID) ON DELETE CASCADE,
    INDEX idx_vehicle_city (vehicleID, cityID) -- optimizes vehicle queries by city
); -- a car belongs to only one city at a time


SHOW VARIABLES LIKE 'max_connections';
SET GLOBAL max_connections = 200;



SELECT * FROM HostCustomers WHERE customerID = 1;
SELECT * FROM ContactInfo WHERE customerID = 1;
SELECT * FROM Auth WHERE customerID = 1;



show tables;

select * from auth JOIN Customers using (customerID) JOIN contactInfo using (customerID);
select * from customers;
select * from contactinfo;
select * from HostCustomers;

select * from city;
select * from vehicles;
select * from hostVehicle;
select * from carstatus;
select * from vehicleCity;
select * from payments;
select * from leases;
SELECT * FROM CarStatus WHERE vehicleID = 1;


delete from vehicles where vehicleID=4;



-- ================================================  INSERTING VALUES   ===================================================================

INSERT INTO Customers (firstName, lastName, licenceNumber) VALUES 
('Riya', 'Dass', 'DL1234567'),
('Nina', 'John', 'AL5432167'),
('Eve', 'White', 'CL3333367'),
('Jiya', 'Dev', 'DL7687654');

INSERT INTO ContactInfo (customerID, email, phoneNumber, address, role) VALUES 
(1, 'riya@gmail.com', '9234567890', 'Street A,Chennai', 'CUSTOMER'),
(2, 'nina@gmail.com', '8345678901', 'Street B,Chennai', 'HOST'),
(3, 'eve@gmail.com', '7678901234', 'Street E-Bangalore', 'ADMIN'),
(4,'jiya@gmail.com', '9887765698', 'Street-X, Chennai', 'HOST');

INSERT INTO Auth (customerID, username, password) VALUES 
(1, 'riyaDass', 'riya123'),
(2, 'ninaHost', 'nina123'),
(3, 'eve_admin', 'admin123'),
(4, 'jiyaDev', 'jiya123');

INSERT INTO HostCustomers (customerID, hostingCityID, gstNumber) VALUES 
(2, 1, '33ABCDE1234F1Z7'),
(4, 2, '29PQRST5678G1Z2');

INSERT INTO City (cityName, state) VALUES 
('Chennai','Tamilnadu'),
('Tripur','Tamilnadu');

INSERT INTO Vehicles (make, model, manufacturingYear, dailyRate, passengerCapacity, engineCapacity, rating, totalRating, ratingCount) VALUES 
('Toyota', 'Camry', 2020, 500.00, 5, 2.5, 4.5, 45, 10),
('Honda', 'Civic', 2019, 450.00, 5, 2.0, 4.2, 42, 10),
('Hyundai', 'i10 grande', 2010, 500.00, 5, 3.2, 1.8, 33, 13),
( 'Tata','nexon','2020',30.0 , 4, 2.8 , 2.2, 12, 3);

INSERT INTO VehicleCity (vehicleID, cityID) VALUES 
(1, 1),
(2, 1),
(3, 2),
(4, 2);

INSERT INTO HostVehicle (hostCustomerID, vehicleID) VALUES 
(1, 1),
(1, 2),
(2, 3),
(2, 4);


INSERT INTO CarStatus (vehicleID, status) VALUES 
(1, 'AVAILABLE'),
(2, 'AVAILABLE'),
(3, 'AVAILABLE'),
(4, 'AVAILABLE');

INSERT INTO Customers (firstName, lastName, licenceNumber) VALUES 
('priya', 'john', 'DL1234552');

INSERT INTO ContactInfo (customerID, email, phoneNumber, address, role) VALUES 
(5, 'priya@gmail.com', '8776654543', 'Street A,Chennai', 'CUSTOMER');

INSERT INTO Auth (customerID, username, password) VALUES 
(5, 'priya', 'priya123');













-- ===================================================  ASSIGNMENT TASKS   ================================================================
-- INSERT INTO City (cityName) VALUES 
-- ('New York'),
-- ('Los Angeles'),
-- ('Chicago'),
-- ('Houston'),
-- ('San Francisco');

-- INSERT INTO Customer (firstName, lastName) VALUES 
-- ('John', 'Doe'),
-- ('Alice', 'Smith'),
-- ('Michael', 'Johnson'),
-- ('Emma', 'Brown'),
-- ('David', 'Wilson');

-- INSERT INTO ContactInfo (customerID, email, phoneNumber) VALUES 
-- (1, 'john.doe@email.com', '1234567890'),
-- (2, 'alice.smith@email.com', '2345678901'),
-- (3, 'michael.johnson@email.com', '3456789012'),
-- (4, 'emma.brown@email.com', '4567890123'),
-- (5, 'david.wilson@email.com', '5678901234');

-- INSERT INTO Vehicle (make, model, manufacturingYear, dailyRate, passengerCapacity, engineCapacity) VALUES 
-- ('Toyota', 'Corolla', 2022, 50.00, 5, 1.8),
-- ('Honda', 'Civic', 2023, 55.00, 5, 2.0),
-- ('Ford', 'Focus', 2021, 45.00, 5, 1.6),
-- ('Chevrolet', 'Malibu', 2020, 40.00, 5, 1.5),
-- ('Tesla', 'Model 3', 2023, 100.00, 5, 0.0), -- Electric Car
-- ('BMW', 'X5', 2023, 90.00, 7, 3.0),
-- ('Mercedes', 'C-Class', 2023, 85.00, 5, 2.5);

-- INSERT INTO HostCustomer (customerID, hostingCityID) VALUES 
-- (1, 1),  -- John Doe hosts in New York
-- (2, 2),  -- Alice Smith hosts in Los Angeles
-- (3, 3);  -- Michael Johnson hosts in Chicago



-- INSERT INTO CarStatus (status, vehicleID) VALUES 
-- ('available', 1),
-- ('notAvailable', 2),
-- ('available', 3),
-- ('available', 4),
-- ('notAvailable', 5),
-- ('available', 6),
-- ('notAvailable', 7);

-- INSERT INTO HostVehicle (hostID, vehicleID) VALUES 
-- (1, 1), -- John Doe hosts Toyota Corolla
-- (1, 2), -- John Doe also hosts Honda Civic
-- (2, 3), -- Alice Smith hosts Ford Focus
-- (2, 4), -- Alice Smith also hosts Chevrolet Malibu
-- (3, 5), -- Michael Johnson hosts Tesla Model 3
-- (3, 6); -- Michael Johnson also hosts BMW X5

-- INSERT INTO VehicleCity (vehicleID, cityID) VALUES 
-- (1, 1), -- Toyota Corolla in New York
-- (2, 1), -- Honda Civic in New York
-- (3, 2), -- Ford Focus in Los Angeles
-- (4, 2), -- Chevrolet Malibu in Los Angeles
-- (5, 3), -- Tesla Model 3 in Chicago
-- (6, 3), -- BMW X5 in Chicago
-- (7, 4); -- Mercedes C-Class in Houston

-- INSERT INTO Lease (vehicleID, customerID, startDate, endDate, leaseType) VALUES 
-- (1, 4, '2024-03-01', '2024-03-07', 'Daily'),
-- (2, 5, '2024-03-05', '2024-03-12', 'Daily'),
-- (3, 1, '2024-02-20', '2024-03-20', 'Monthly'),
-- (4, 2, '2024-02-15', '2024-02-25', 'Daily'),
-- (5, 3, '2024-03-01', '2024-04-01', 'Monthly');

-- INSERT INTO Payment (leaseID, paymentDate, amount) VALUES 
-- (1, '2024-03-07', 350.00),
-- (2, '2024-03-12', 385.00),
-- (3, '2024-03-20', 1350.00),
-- (4, '2024-02-25', 400.00),
-- (5, '2024-04-01', 3000.00);

-- select * from customer;
-- use carrentaldb;

-- show tables;
-- select * from city;
-- Select * from vehiclecity;
-- select * from vehicle;

-- -- Find the total revenue generated from all leases
-- SELECT SUM(amount) AS total_revenue FROM Payment;

-- -- Find the average daily rental price of all vehicles
-- SELECT AVG(dailyRate) AS avg_daily_rate FROM Vehicle;

-- -- Find all active leases as of today
-- SELECT * FROM Lease WHERE '2024-02-20' BETWEEN startDate AND endDate;
-- select * from lease;

-- -- Find the number of days a customer has rented a car
-- SELECT leaseID, DATEDIFF(endDate, startDate) AS total_days FROM Lease;

-- -- Retrieve customers whose email contains 'gmail'
-- SELECT * FROM ContactInfo WHERE email LIKE '%email%';

-- -- Convert all vehicle makes to uppercase
-- SELECT UPPER(make) AS UpperCaseMake FROM Vehicle;

-- -- Find customers who have rented a vehicle in a specific city (e.g., 'New York')
-- set @city='New York';
-- SELECT * FROM Customer 
-- WHERE customerID IN (
--     SELECT DISTINCT lease.customerID 
--     FROM Lease lease
--     JOIN VehicleCity vc ON lease.vehicleID = vc.vehicleID
--     JOIN City c ON vc.cityID = c.cityID
--     WHERE c.cityName = 'New York'
-- );

-- -- Find vehicles that have never been leased
-- SELECT * FROM Vehicle v
-- WHERE NOT EXISTS (
--     SELECT 1 FROM Lease l WHERE l.vehicleID = v.vehicleID
-- );

-- -- Find customers who have rented more than one vehicle
-- SELECT c.customerID, c.firstName, c.lastName 
-- FROM Customer c
-- WHERE (
--     SELECT COUNT(*) FROM Lease l WHERE l.customerID = c.customerID
-- ) > 1;
-- SELECT * FROM lease;

-- -- retrieve all available vehicles for rent in a specific city
-- DELIMITER $$
-- CREATE PROCEDURE GetAvailableVehiclesByCity(IN cityNameParam VARCHAR(100))
-- BEGIN
--     SELECT 
-- 		v.vehicleID, 
--         v.make, 
--         v.model, 
--         v.manufacturingYear, 
--         v.dailyRate, 
--         v.passengerCapacity, 
--         v.engineCapacity
--     FROM Vehicle v
--     JOIN CarStatus cs ON v.vehicleID = cs.vehicleID
--     JOIN VehicleCity vc ON v.vehicleID = vc.vehicleID
--     JOIN City c ON vc.cityID = c.cityID
--     WHERE cs.status = 'available' AND c.cityName = cityNameParam;
-- END $$
-- DELIMITER ;
-- CALL GetAvailableVehiclesByCity('Los Angeles');

-- -- Find all customers and their total amount spent on rentals
-- WITH CustomerPayments AS (
--     SELECT l.customerID, SUM(p.amount) AS total_spent
--     FROM Lease l
--     JOIN Payment p ON l.leaseID = p.leaseID
--     GROUP BY l.customerID
-- )
-- SELECT c.customerID, c.firstName, c.lastName, cp.total_spent
-- FROM Customer c
-- JOIN CustomerPayments cp ON c.customerID = cp.customerID;

-- -- Rank customers based on their total spending
-- SELECT 
-- 	c.customerID, 
-- 	c.firstName, 
--     c.lastName, 
--     SUM(p.amount) AS total_spent,
-- 	RANK() OVER (ORDER BY SUM(p.amount) DESC) AS spending_rank
-- FROM Customer c
-- JOIN Lease l ON c.customerID = l.customerID
-- JOIN Payment p ON l.leaseID = p.leaseID
-- GROUP BY c.customerID;




