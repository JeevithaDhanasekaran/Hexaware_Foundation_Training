/*
Database Tables:
1. Customers:
• CustomerID (Primary Key)
• FirstName
• LastName
• Email
• Phone
• Address

2. Products:
• ProductID (Primary Key)
• ProductName
• Description
• Price


3. Orders:
• OrderID (Primary Key)
• CustomerID (Foreign Key referencing Customers)
• OrderDate
• TotalAmount

4. OrderDetails:
• OrderDetailID (Primary Key)
• OrderID (Foreign Key referencing Orders)
• ProductID (Foreign Key referencing Products)
• Quantity

5. Inventory
• InventoryID (Primary Key)
• ProductID (Foreign Key referencing Products)
• QuantityInStock
• LastStockUpdate

*/

/*Task:1. Database Design:
1.Create the database named "TechShop"*/
CREATE DATABASE IF NOT EXISTS TechShop;
USE TechShop;

/*2. Define the schema for the Customers, Products, Orders, OrderDetails and Inventory tables
based on the provided schema.*/

-- Customers Table
CREATE TABLE Customers (
    CustomerID INT AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Address TEXT,
    CONSTRAINT customers_pk PRIMARY KEY (CustomerID)
);

-- Products Table
CREATE TABLE Products (
    ProductID INT AUTO_INCREMENT,
    ProductName VARCHAR(100) NOT NULL,
    Description TEXT,
    Price DECIMAL(10,2) NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (ProductID)
);

-- Orders Table
CREATE TABLE Orders (
    OrderID INT AUTO_INCREMENT,
    CustomerID INT NOT NULL,
    OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TotalAmount DECIMAL(10,2) NOT NULL,
    CONSTRAINT orders_pk PRIMARY KEY (OrderID),
    CONSTRAINT orders_fk_customer FOREIGN KEY (CustomerID) 
        REFERENCES Customers(CustomerID) ON DELETE CASCADE
);

-- OrderDetails Table
CREATE TABLE OrderDetails (
    OrderDetailID INT AUTO_INCREMENT,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0), -- to ensure integrity
    CONSTRAINT orderdetails_pk PRIMARY KEY (OrderDetailID),
    CONSTRAINT orderdetails_fk_order FOREIGN KEY (OrderID) 
        REFERENCES Orders(OrderID) ON DELETE CASCADE,
    CONSTRAINT orderdetails_fk_product FOREIGN KEY (ProductID) 
        REFERENCES Products(ProductID) ON DELETE CASCADE
);

-- Inventory Table
CREATE TABLE Inventory (
    InventoryID INT AUTO_INCREMENT,
    ProductID INT NOT NULL,
    QuantityInStock INT NOT NULL CHECK (QuantityInStock >= 0),
    LastStockUpdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT inventory_pk PRIMARY KEY (InventoryID),
    CONSTRAINT inventory_fk_product FOREIGN KEY (ProductID) 
        REFERENCES Products(ProductID) ON DELETE CASCADE
);

/*
4.Create appropriate Primary Key and Foreign Key constraints for referential integrity.

Table relationships:
Primary Key:
CustomerID in Customers
ProductID in Products
OrderID in Orders
OrderDetailID in OrderDetails
InventoryID in Inventory

Foreign Key:
Orders.CustomerID -> References Customers.CustomerID
OrderDetails.OrderID -> References Orders.OrderID 
OrderDetails.ProductID ->  References Products.ProductID
Inventory.ProductID -> References Products.ProductID
*/

-- 5. Insert at least 10 sample records into each of the following tables. 

INSERT INTO Customers (FirstName, LastName, Email, Phone, Address) VALUES
('John', 'Doe', 'john.doe@example.com', '9876543210', '123 Main St, CityA'),
('Alice', 'Smith', 'alice.smith@example.com', '9876543211', '456 Elm St, CityB'),
('Bob', 'Johnson', 'bob.johnson@example.com', '9876543212', '789 Pine St, CityC'),
('Charlie', 'Brown', 'charlie.brown@example.com', '9876543213', '101 Oak St, CityD'),
('David', 'Wilson', 'david.wilson@example.com', '9876543214', '202 Maple St, CityE'),
('Emma', 'Jones', 'emma.jones@example.com', '9876543215', '303 Birch St, CityF'),
('Frank', 'Taylor', 'frank.taylor@example.com', '9876543216', '404 Cedar St, CityG'),
('Grace', 'Anderson', 'grace.anderson@example.com', '9876543217', '505 Walnut St, CityH'),
('Henry', 'White', 'henry.white@example.com', '9876543218', '606 Willow St, CityI'),
('Ivy', 'Martinez', 'ivy.martinez@example.com', '9876543219', '707 Ash St, CityJ');

INSERT INTO Products (ProductName, Description, Price) VALUES
('Laptop', 'High-performance laptop', 1200.99),
('Smartphone', 'Latest Android phone', 799.50),
('Tablet', '10-inch screen tablet', 499),
('Headphones', 'Noise-canceling headphones', 199.99),
('Smartwatch', 'Waterproof smartwatch', 149.95),
('Keyboard', 'Mechanical keyboard', 89.99),
('Mouse', 'Wireless optical mouse', 49.50),
('Monitor', '27-inch 4K monitor', 349),
('Printer', 'Laser printer', 199),
('External Hard Drive', '1TB storage device', 79.99);

INSERT INTO Orders (CustomerID, TotalAmount) VALUES
(1, 1299.99),
(2, 899.50),
(3, 149.95),
(4, 539.99),
(5,  89.99),
(6,  199.00),
(7, 499.00),
(8,  1200.99),
(9,  349.00),
(10, 79.99);

INSERT INTO OrderDetails (OrderID, ProductID, Quantity) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 5, 1),
(4, 4, 2),
(5, 6, 1),
(6, 9, 1),
(7, 3, 1),
(8, 1, 1),
(9, 8, 1),
(10, 10, 2);

INSERT INTO Inventory (ProductID, QuantityInStock) VALUES
(1, 50),
(2, 30),
(3, 20),
(4, 40),
(5, 25),
(6, 60),
(7, 35),
(8, 15),
(9, 45),
(10, 10);

-- Tasks 2: Select, Where, Between, AND, LIKE:
-- 1.Write an SQL query to retrieve the names and emails of all customers.
SELECT concat(FirstName,' ',LastName) as Name,email from Customers;

-- 2. Write an SQL query to list all orders with their order dates and corresponding customer
-- names.
select concat(customers.firstname,' ',customers.lastname) as customerName,
		orders.orderID,orders.orderDate from orders inner join
        customers using (customerID) ;

-- 3. Write an SQL query to insert a new customer record into the "Customers" table. Include
-- customer information such as name, email, and address.
insert into customers(Firstname,LastName,Email,Phone,Address) values('Jeevitha','Dhanasekaran','jeevitha@gmail.com',1234567890,'tamilnadu-Namakkal');

-- 4. Write an SQL query to update the prices of all electronic gadgets in the "Products" table by
-- increasing them by 10%
SET SQL_SAFE_UPDATES = 0;
update products set price=price * 1.10 ;
select * from products;

-- 5. Write an SQL query to delete a specific order and its associated order details from the
-- "Orders" and "OrderDetails" tables. Allow users to input the order ID as a parameter.
set @userDeleteID= 1;
delete from orders where orderID=@userDeleteID;

-- 6. Write an SQL query to insert a new order into the "Orders" table. Include the customer ID,
-- order date, and any other necessary information.
insert into orders (customerID,TotalAmount) values(4,4900);

-- 7. Write an SQL query to update the contact information (e.g., email and address) of a specific
-- customer in the "Customers" table. Allow users to input the customer ID and new contact
-- information.
set @NewEmail='newmail@gmail.com';
set @NewAddress='guindy-chennai';
set @newCustomerID=11;
update customers set email=@NewEmail, address=@NewAddress where customerID= @newCustomerID;

select * from customers;

-- 8. Write an SQL query to recalculate and update the total cost of each order in the "Orders"
-- table based on the prices and quantities in the "OrderDetails" table.
alter table orders add column totalCost decimal(10,2) not null default 0;
select * from orders;

update orders join 
	(select 
		orderDetails.orderID,
		sum(orderDetails.quantity*products.price) as totCost 
        from
		orderDetails left join products 
        using (productID) 
	group by orderDetails.orderID)as cost_table 
on orders.orderID =cost_table.orderID 
set orders.TotalAmount=cost_table.totCost  ;

-- 9. Write an SQL query to delete all orders and their associated order details for a specific
-- customer from the "Orders" and "OrderDetails" tables. Allow users to input the customer ID
-- as a parameter.
set @inputcustomerID=2;
delete from orders where customerID= @inputcustomerID;

-- 10. Write an SQL query to insert a new electronic gadget product into the "Products" table,
-- including product name, category, price, and any other relevant details.
INSERT INTO Products (ProductName, Description, Price)
VALUES ('Smartphone', 'Latest 5G smartphone with 128GB storage', 699.99);

-- 11. Write an SQL query to update the status of a specific order in the "Orders" table (e.g., from
-- "Pending" to "Shipped"). Allow users to input the order ID and the new status
Alter Table Orders add column Status ENUM('Pending', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending';
set @NewStatus='Shipped';
set @orderId=10;
update orders set status=@NewStatus where orderid=@orderId;
select * from orders left join orderDetails using (orderID);

-- 12. Write an SQL query to calculate and calculate and update the number of orders placed by each customer
-- in the "Customers" table based on the data in the "Orders" table 
select customers.customerID, concat(customers.firstname,' ',customers.lastname) as customerName ,count(orders.orderID) as totOrders
from Customers left join orders using (customerID)
group by customers.customerID;

/*
Task 3. Aggregate functions, Having, Order By, GroupBy and Joins:

1. Write an SQL query to retrieve a list of all orders along with customer information (e.g.,
customer name) for each order.
*/
select * from orderDetails;
select 
	customers.customerID, 
    concat(customers.firstname,' ',customers.lastname) as customerName ,
    orders.OrderID,
    orders.totalAmount,
    orders.status 
from orders left join customers using (customerID)
group by orders.orderID;

-- 2. Write an SQL query to find the total revenue generated by each electronic gadget product.
-- Include the product name and the total revenue
select
	products.productID,
    products.productName,
    ifnull(sum(products.price*orderDetails.quantity),0) as totalRevenue
from products left join orderDetails using (productID) 
group by products.productID, products.productName;

-- 3. Write an SQL query to list all customers who have made at least one purchase. Include their
-- names and contact information
select 
	customers.customerID,
	concat(customers.firstname,' ',customers.lastname) as customerName,
    customers.email,
    customers.phone,
    count(orders.orderID) as numberOfOrders
from customers left join orders using (customerID)
group by customers.customerID
having numberOfOrders>0;

-- 4. Write an SQL query to find the most popular electronic gadget, which is the one with the highest
-- total quantity ordered. Include the product name and the total quantity ordered
update orderDetails set quantity= 5 where orderDetailId=3;

select products.productName,sum(orderDetails.quantity) as totalQuantity from products 
join orderDetails using (productID) group by products.productID
order by totalQuantity desc limit 1;

-- 5. Write an SQL query to retrieve a list of electronic gadgets along with their corresponding
-- categories.
alter table products add column productline text not null;
update products set productline= 
	case
		when productName in ('Laptop','Smartphone','Tablet') then 'Smart Computing Devices'
		when productName in ('Smartwatch','Earphones','Headphones') then 'Wearable Devices'
		when productname in ('Keyboard','Mouse','Monitor','Printer') then 'computer accessories'
		when productName in ('External Hard Drive') then 'Storage Devices'
		else 'others'
	end;
SET SQL_SAFE_UPDATES=1;
select productName, productline from products;

-- 6. Write an SQL query to calculate the average order value for each customer. Include the
-- customer's name and their average order value.
select 
	customers.customerID,
	concat(customers.firstname,' ',customers.lastname) as customerName,
    sum(totalAmount)
from customers join orders using (customerID) group by customerID;

-- 7. Write an SQL query to find the order with the highest total revenue. Include the order ID,
-- customer information, and the total revenue.
select 
	customers.customerID,
    concat(customers.firstname,' ',customers.lastname) as customerName,
    orders.TotalAmount as TotalRevenue
from customers join orders using (customerID)
order by TotalRevenue desc limit 1;

-- 8. Write an SQL query to list electronic gadgets and the number of times each product has been
-- ordered.
select 
	products.productID,
	products.productName,
    count(orderDetails.orderID) as NumberOfTimesOrdered
from products left join orderDetails using (productId)-- if a product is never ordered , then include that also using left join 
group by products.productID;

-- 9. Write an SQL query to find customers who have purchased a specific electronic gadget product.
-- Allow users to input the product name as a parameter.
select * from products;
set @productInput='Smartphone';
set @productInput='Tablet';

select
	products.productID,
	products.productName,
	coalesce( orders.orderID,'Not Ordered') as OrderID,
	coalesce(customers.customerID,'No customer') as CustomerID,
	coalesce(concat(customers.firstName,' ',customers.lastName),'No customer') as CustomerName
from products 
	left join orderDetails using (ProductID)
    left join orders using (orderID)
    left join customers using (customerID)
where productName=@productInput;

-- 10. Write an SQL query to calculate the total revenue generated by all orders placed within a
-- specific time period. Allow users to input the start and end dates as parameters.
set @startDate='2025-04-20';
set @endDate='2025-05-22';
select orderID,OrderDate,TotalAmount from orders where orderDate between Date(@startDate) and date(@endDate);

-- Task 4. Subquery and its type:

-- 1. Write an SQL query to find out which customers have not placed any orders.
select * from customers;
insert into customers(FirstName,lastName,Email,Phone,Address) 
	values ('Millie','Bobby brown','millie@gmail.com',9876453265,'567,hawkins'),
    ('Nina','Dobrev','nina@gmail.com',7634653241,'134-North Mystic Falls');
-- select 
-- 	customers.customerID,
--     concat(customers.firstName,' ',customers.lastName)as customerName,
--     orders.orderID
-- from customers left join orders using (customerID) where isnull(orderID);
select 
	customers.customerID,
    concat(customers.firstName,' ',customers.lastName)as customerName
    from customers 
where customers.customerID not in (select distinct customerID from orders);

-- 2. Write an SQL query to find the total number of products available for sale.
select 
	count(*)as ProductAvailable 
from products where productID in (select productID from inventory where QuantityInStock>0);

-- 3. Write an SQL query to calculate the total revenue generated by TechShop.
select
	sum(TotalAmount) as RevenueEarned from orders;
desc orders;

-- 4. Write an SQL query to calculate the average quantity ordered for products in a specific category.
-- Allow users to input the category name as a parameter.
INSERT INTO orderDetails (orderID, productID, quantity) VALUES
(7, 3, 2),
(8, 1, 3);

set @category='Smart Computing Devices';
select * from products;
select
    avg(quantity)as averageQuantity
from orderDetails where productID in (select productID from products where productline=@category);

-- 5. Write an SQL query to calculate the total revenue generated by a specific customer. Allow users
-- to input the customer ID as a parameter.
set @customerIDinput=4;
select SUM(TotalAmount)as totalRevenue from orders where customerID=@customerIDinput group by customerID;

-- 6. Write an SQL query to find the customers who have placed the most orders. List their names
-- and the number of orders they've placed.
select
	customers.customerID,
    concat(customers.firstName,' ',customers.lastName) as CustomerName,
    count(orders.orderID)as MaxOrders
from customers join orders using (customerID) 
group by customers.customerID having count(orders.orderID)= 
(
	select Max(orderCount) as max 
    from (
		select 
		customerID,
        count(orderID)as orderCount 
		from orders group by customerID) as maxCount
);

-- 7. Write an SQL query to find the most popular product category, which is the one with the highest
-- total quantity ordered across all orders.

select productline,ordersCount from (
	select 
		products.productline,
		sum(orderDetails.quantity)as ordersCount
	from products join orderDetails using(productID)
	group by products.productline) as HighestCategory
order by ordersCount desc limit 1;

-- 8. Write an SQL query to find the customer who has spent the most money (highest total revenue)
-- on electronic gadgets. List their name and total spending.

select
	customers.customerID,
    customers.firstName,
    orders.TotalAmount
from customers join orders using (CustomerID)
where orders.TotalAmount = (
	select max(TotalAmount) from orders
);

-- 9. Write an SQL query to calculate the average order value (total revenue divided by the number of
-- orders) for all customers.
select 
	revenuePerOrder.customerID,
    revenuePerOrder.CustomerName,
    sum(revenuePerOrder.TotalAmount)as TotalRevenue,
    count(revenuePerOrder.orderID) as countOfOrders,
    format(sum(revenuePerOrder.TotalAmount)/count(revenuePerOrder.orderID),2) as AverageOrderValue
    from 
		(select
		customers.customerID,
		concat(customers.firstName,' ',customers.lastName) as CustomerName,
		orders.TotalAmount,
		orders.orderID
		from customers join orders using (CustomerID))as revenuePerOrder 
group by customerID;

-- 10. Write an SQL query to find the total number of orders placed by each customer and list their
-- names along with the order count.
select 
	revenuePerOrder.customerID,
    revenuePerOrder.CustomerName,
    count(revenuePerOrder.orderID) as countOfOrders
    from 
		(select
		customers.customerID,
		concat(customers.firstName,' ',customers.lastName) as CustomerName,
		orders.orderID
		from customers join orders using (CustomerID))as revenuePerOrder 
group by customerID;

select * from customers;
select * from orders;
select * from orderDetails;
select * from inventory;
select * from products;