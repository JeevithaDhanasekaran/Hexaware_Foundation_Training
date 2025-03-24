create database CourierManagementDB;
use CourierManagementDB;

/*

Task1 Database Design 
Design a SQL schema for a Courier Management System with tables for Customers, Couriers, Orders, 
and Parcels. Define the relationships between these tables using appropriate foreign keys. 
Requirements: 
• Define the Database Schema • Create SQL tables for entities such as User, Courier, Employee, 
Location,Payment 
• Define relationships between these tables (one-to-many, many-to-many, etc.). 
• Populate Sample Data • Insert sample data into the tables to simulate real-world scenarios. 
User Table: 
User 
(UserID INT PRIMARY KEY, 
Name VARCHAR(255), 
Email VARCHAR(255) UNIQUE, 
Password VARCHAR(255), 
ContactNumber VARCHAR(20), 
Address TEXT 
); 

Courier 
(CourierID INT PRIMARY KEY, 
SenderName VARCHAR(255), 
SenderAddress TEXT, 
ReceiverName VARCHAR(255), 
ReceiverAddress TEXT, 
Weight DECIMAL(5, 2), 
Status VARCHAR(50), 
TrackingNumber VARCHAR(20) UNIQUE, 
DeliveryDate DATE); 

CourierServices 
(ServiceID INT PRIMARY KEY, 
ServiceName VARCHAR(100), 
Cost DECIMAL(8, 2)); 

Employee Table: 
(EmployeeID INT PRIMARY KEY, 
Name VARCHAR(255), 
Email VARCHAR(255) UNIQUE, 
ContactNumber VARCHAR(20), 
Role VARCHAR(50), 
Salary DECIMAL(10, 2)); 


Location Table: 
(LocationID INT PRIMARY KEY, 
LocationName VARCHAR(100), 
Address TEXT); 

Payment Table: 
(PaymentID INT PRIMARY KEY, 
CourierID INT, 
LocationId INT, 
Amount DECIMAL(10, 2), 
PaymentDate DATE, 
FOREIGN KEY (CourierID) REFERENCES Couriers(CourierID), 
FOREIGN KEY (LocationID) REFERENCES Location(LocationID));
*/
CREATE TABLE User (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    ContactNumber VARCHAR(20) NOT NULL,
    Address TEXT NOT NULL
);

CREATE TABLE Courier (
    CourierID INT PRIMARY KEY AUTO_INCREMENT,
    SenderName VARCHAR(255) NOT NULL,
    SenderAddress TEXT NOT NULL,
    ReceiverName VARCHAR(255) NOT NULL,
    ReceiverAddress TEXT NOT NULL,
    Weight DECIMAL(5,2) NOT NULL,
    Status VARCHAR(50) DEFAULT 'Pending',
    TrackingNumber VARCHAR(20) UNIQUE NOT NULL,
    DeliveryDate DATE
);
CREATE TABLE CourierServices (
    ServiceID INT PRIMARY KEY AUTO_INCREMENT,
    ServiceName VARCHAR(100) NOT NULL,
    Cost DECIMAL(8,2) NOT NULL
);

CREATE TABLE Employee (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    ContactNumber VARCHAR(20) NOT NULL,
    Role VARCHAR(50) NOT NULL,
    Salary DECIMAL(10,2) NOT NULL
);

CREATE TABLE Location (
    LocationID INT PRIMARY KEY AUTO_INCREMENT,
    LocationName VARCHAR(100) NOT NULL,
    Address TEXT NOT NULL
);

CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY AUTO_INCREMENT,
    CourierID INT NOT NULL,
    LocationID INT NOT NULL,
    Amount DECIMAL(10,2) NOT NULL,
    PaymentDate DATE NOT NULL,
    FOREIGN KEY (CourierID) REFERENCES Courier(CourierID) ON DELETE CASCADE,
    FOREIGN KEY (LocationID) REFERENCES Location(LocationID) ON DELETE CASCADE
);

CREATE TABLE Orders (
    OrderID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT NOT NULL,
    CourierID INT NOT NULL,
    OrderDate DATE NOT NULL DEFAULT (CURRENT_DATE),
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE,
    FOREIGN KEY (CourierID) REFERENCES Courier(CourierID) ON DELETE CASCADE
);

/*
Table RelationShip:
User & Orders: One-to-Many (A user can place multiple orders).
Orders & Courier: One-to-One (Each order is linked to a single courier).
Courier & Payment: One-to-One (A courier has one payment).
Courier & Location: Many-to-One (A courier belongs to one location).
CourierServices: independent table defining service types.
*/

INSERT INTO User (Name, Email, Password, ContactNumber, Address) VALUES
('John Doe', 'john@example.com', 'pass123', '9876543210', '123 Main St, City A'),
('Jane Smith', 'jane@example.com', 'pass456', '8765432109', '456 Elm St, City B'),
('Alice Brown', 'alice@example.com', 'pass789', '7654321098', '789 Pine Rd, City C'),
('Bob White', 'bob@example.com', 'pass101', '6543210987', '321 Oak St, City D'),
('Eve Johnson', 'eve@example.com', 'pass102', '5432109876', '852 Maple Ave, City E');

INSERT INTO CourierServices (ServiceName, Cost) VALUES
('Standard Delivery', 10.00),
('Express Delivery', 25.00),
('Overnight Delivery', 50.00),
('Same-Day Delivery', 35.00),
('International Shipping', 100.00);

INSERT INTO Employee (Name, Email, ContactNumber, Role, Salary) VALUES
('Charlie Green', 'charlie@courier.com', '9123456789', 'Manager', 60000),
('Dave Black', 'dave@courier.com', '9234567890', 'Delivery Executive', 35000),
('Sam Wilson', 'sam@courier.com', '9345678901', 'Delivery Executive', 37000),
('Tina Adams', 'tina@courier.com', '9456789012', 'Support Staff', 30000),
('Mike Reed', 'mike@courier.com', '9567890123', 'Manager', 62000);

INSERT INTO Location (LocationName, Address) VALUES
('Warehouse A', '789 Industrial Park, City A'),
('Warehouse B', '321 Logistics Hub, City B'),
('Sorting Facility C', '654 Transit Lane, City C'),
('Delivery Hub D', '987 Express St, City D'),
('International Terminal', '111 Global Rd, City E');

INSERT INTO Courier (SenderName, SenderAddress, ReceiverName, ReceiverAddress, Weight, Status, TrackingNumber, DeliveryDate) VALUES
('John Doe', '123 Main St, City A', 'Jane Smith', '456 Elm St, City B', 2.50, 'In Transit', 'TRK123456', '2025-03-30'),
('Alice Brown', '789 Pine Rd, City C', 'Bob White', '321 Oak St, City D', 5.75, 'Delivered', 'TRK654321', '2025-03-28'),
('Eve Johnson', '852 Maple Ave, City E', 'Charlie Green', '789 Industrial Park, City A', 1.20, 'Pending', 'TRK789456', '2025-04-01'),
('Bob White', '321 Oak St, City D', 'Tina Adams', '987 Express St, City D', 3.40, 'Out for Delivery', 'TRK567890', '2025-03-29'),
('Jane Smith', '456 Elm St, City B', 'Mike Reed', '111 Global Rd, City E', 7.80, 'Shipped', 'TRK908765', '2025-04-02');

INSERT INTO Payment (CourierID, LocationID, Amount, PaymentDate) VALUES
(1, 1, 10.00, '2025-03-24'),
(2, 2, 25.00, '2025-03-25'),
(3, 3, 50.00, '2025-03-26'),
(4, 4, 35.00, '2025-03-27'),
(5, 5, 100.00, '2025-03-28');

INSERT INTO Orders (UserID, CourierID, OrderDate) VALUES
(1, 1, '2025-03-22'),
(2, 2, '2025-03-23'),
(3, 3, '2025-03-24'),
(4, 4, '2025-03-25'),
(5, 5, '2025-03-26');

/*
Task 2: Select,Where
Solve the following queries in the Schema that you have created above */

-- 1. List all customers: 
select * from user;

-- 2. List all orders for a specific customer: 
INSERT INTO Courier (SenderName, SenderAddress, ReceiverName, ReceiverAddress, Weight, Status, TrackingNumber, DeliveryDate) VALUES
('Paul Wesley', 'rdx Main St, City y', 'Nina Dobrev', '456 new St, City B', 9.50, 'In Transit', 'TRK123498', '2025-03-30');
INSERT INTO Orders (UserID, CourierID, OrderDate) VALUES
(1, 6, '2025-03-25');

set @input=1;
select * from user join orders using (userID) where userID=@input;

-- 3. List all couriers: 
select * from courier;

-- 4. List all packages for a specific order: 

-- 5. List all deliveries for a specific courier: 
-- 6. List all undelivered packages: 
-- 7. List all packages that are scheduled for delivery today: 
-- 8. List all packages with a specific status: 
-- 9. Calculate the total number of packages for each courier. 
-- 10. Find the average delivery time for each courier 
-- 11. List all packages with a specific weight range: 
-- 12. Retrieve employees whose names contain 'John' 
-- 13. Retrieve all courier records with payments greater than $50. 
-- Task 3: GroupBy, Aggregate Functions, Having, Order By, where 
-- 14. Find the total number of couriers handled by each employee. 
-- 15. Calculate the total revenue generated by each location 
-- 16. Find the total number of couriers delivered to each location. 
-- 17. Find the courier with the highest average delivery time: 
-- 18. Find Locations with Total Payments Less Than a Certain Amount 
-- 19. Calculate Total Payments per Location 
-- 20. Retrieve couriers who have received payments totaling more than $1000 in a specific location 
-- (LocationID = X): 
-- 21. Retrieve couriers who have received payments totaling more than $1000 after a certain date 
-- (PaymentDate > 'YYYY-MM-DD'): 
-- 22. Retrieve locations where the total amount received is more than $5000 before a certain date 
-- (PaymentDate > 'YYYY-MM-DD') 
-- Task 4: Inner Join,Full Outer Join, Cross Join, Left Outer Join,Right Outer Join 
-- 23. Retrieve Payments with Courier Information 
-- 24. Retrieve Payments with Location Information 
-- 25. Retrieve Payments with Courier and Location Information 
-- 26. List all payments with courier details 
-- 27. Total payments received for each courier 
-- 28. List payments made on a specific date 
-- © Hexaware Technologies Limited. All rights www.hexaware.com
-- 29. Get Courier Information for Each Payment 
-- 30. Get Payment Details with Location 
-- 31. Calculating Total Payments for Each Courier 
-- 32. List Payments Within a Date Range 
-- 33. Retrieve a list of all users and their corresponding courier records, including cases where there are 
-- no matches on either side 
-- 34. Retrieve a list of all couriers and their corresponding services, including cases where there are no 
-- matches on either side 
-- 35. Retrieve a list of all employees and their corresponding payments, including cases where there are 
-- no matches on either side 
-- 36. List all users and all courier services, showing all possible combinations. 
-- 37. List all employees and all locations, showing all possible combinations: 
-- 38. Retrieve a list of couriers and their corresponding sender information (if available) 
-- 39. Retrieve a list of couriers and their corresponding receiver information (if available): 
-- 40. Retrieve a list of couriers along with the courier service details (if available): 
-- 41. Retrieve a list of employees and the number of couriers assigned to each employee: 
-- 42. Retrieve a list of locations and the total payment amount received at each location: 
-- 43. Retrieve all couriers sent by the same sender (based on SenderName). 
-- 44. List all employees who share the same role. 
-- 45. Retrieve all payments made for couriers sent from the same location. 
-- 46. Retrieve all couriers sent from the same location (based on SenderAddress). 
-- 47. List employees and the number of couriers they have delivered: 
-- 48. Find couriers that were paid an amount greater than the cost of their respective courier services 
-- Scope: Inner Queries, Non Equi Joins, Equi joins,Exist,Any,All 
-- 49. Find couriers that have a weight greater than the average weight of all couriers 
-- 50. Find the names of all employees who have a salary greater than the average salary: 
-- 51. Find the total cost of all courier services where the cost is less than the maximum cost 
-- 52. Find all couriers that have been paid for 
-- 53. Find the locations where the maximum payment amount was made 
-- 54. Find all couriers whose weight is greater than the weight of all couriers sent by a specific sender 
-- (e.g., 'SenderName'):
