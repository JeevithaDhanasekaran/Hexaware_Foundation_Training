package com.techshop.service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.techshop.dao.OrderDao;
import com.techshop.entities.Customers;
import com.techshop.entities.Orders;
import com.techshop.exceptions.DatabaseConnectionException;
import com.techshop.exceptions.OrderProcessingException;
import com.techshop.util.DBConnUtil;

public class OrderDaoImpl implements OrderDao {

	private Connection connection;

	public OrderDaoImpl() throws Exception {
		try {
			this.connection = DBConnUtil.getConnection();
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Failed to connect to the database.");
		}
	}

	@Override
	public void addOrder(Orders order) throws SQLException, OrderProcessingException {
		String insertQuery = "INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount, OrderStatus) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
			ps.setInt(1, order.getOrderID());
			ps.setInt(2, order.getCustomer().getCustomerID());
			ps.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
			ps.setBigDecimal(4, order.getTotalAmount());
			ps.setString(5, order.getOrderStatus());

			int rows = ps.executeUpdate();
			if (rows == 0) {
				throw new OrderProcessingException("Failed to add the order.");
			}
		}
		catch(SQLException e) {
			throw new OrderProcessingException("Failed to add the order. Reason: " + e.getMessage());
		}
	}

	@Override
	public Orders getOrderById(int orderId) throws SQLException {
		String query = "SELECT * FROM Orders WHERE OrderID = ?";
		Customers customer = new Customers();
		try (PreparedStatement ps = connection.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			ps.setInt(1, orderId);

			if (rs.next()) {
				
				customer.setCustomerID(rs.getInt("CustomerID"));

				return new Orders(
						rs.getInt("OrderID"),
						customer,
						rs.getDate("OrderDate"),
						rs.getBigDecimal("TotalAmount"),
						rs.getString("OrderStatus"),
						null
				);
			}
			else {
				throw new SQLException("Order not found with ID: " + orderId);
			}
		}
	}

	@Override
	public List<Orders> getAllOrders() throws SQLException {
		List<Orders> ordersList = new ArrayList<>();
		String query = "SELECT o.orderID, o.orderDate, o.totalAmount,o.Status, c.customerID, c.firstName, c.lastName FROM orders o JOIN customers c ON o.customerID = c.customerID";

		try (Statement st = connection.createStatement();
			 ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Customers customer = new Customers();
				customer.setCustomerID(rs.getInt("CustomerID"));
				customer.setFirstName(rs.getString("firstName"));
			    customer.setLastName(rs.getString("lastName"));

				Orders order = new Orders(
						rs.getInt("OrderID"),
						customer,
						rs.getDate("OrderDate"),
						rs.getBigDecimal("TotalAmount"),
						rs.getString("Status"),
						null
				);
				order.setCustomer(customer);
				ordersList.add(order);
			}
		}
		return ordersList;
	}

	@Override
	public void updateOrderStatus(int orderId, String status) throws SQLException {
		String updateQuery = "UPDATE Orders SET OrderStatus = ? WHERE OrderID = ?";
		try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
			ps.setString(1, status);
			ps.setInt(2, orderId);

			int rows = ps.executeUpdate();
			if (rows == 0) {
				throw new SQLException("No Order found with ID: " + orderId);
			}
		}
	}

	@Override
	public void cancelOrder(int orderId) throws SQLException {
		final String STATUS_CANCELLED = "Cancelled";
		updateOrderStatus(orderId, STATUS_CANCELLED);
	}

	@Override
	public double calculateTotalAmount(int orderId) throws SQLException {
		String query = "SELECT TotalAmount FROM Orders WHERE OrderID = ?";
		try (PreparedStatement ps = connection.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			ps.setInt(1, orderId);
			

			if (rs.next()) {
				return rs.getBigDecimal("TotalAmount").doubleValue();
			}
		}
		throw new SQLException("Order not found with ID: " + orderId);
	}

	@Override
	public boolean placeOrder(int customerId, int productId, int quantity) throws SQLException {
		String checkQuery = "SELECT stockQuantity, price FROM products WHERE productID=?";
	    try (PreparedStatement pst1 = connection.prepareStatement(checkQuery)) {

	        pst1.setInt(1, productId);
	        ResultSet rs = pst1.executeQuery();

	        if (rs.next()) {
	            int availableQty = rs.getInt("StockQuantity");
	            BigDecimal price = rs.getBigDecimal("Price");

	            if (availableQty >= quantity) {

	                connection.setAutoCommit(false);  // Transaction start
	                
	                // Insert into orders (Main Order)
	                String insertOrder = "INSERT INTO orders(customerID, totalAmount) VALUES(?,?)";
	                PreparedStatement pst2 = connection.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
	                BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));
	                
	                pst2.setInt(1, customerId);
	                pst2.setBigDecimal(2, totalAmount);
	                pst2.executeUpdate();

	                ResultSet orderKeys = pst2.getGeneratedKeys();
	                if(orderKeys.next()) {
	                    int orderId = orderKeys.getInt(1);

	                    // Insert into orderdetails
	                    String insertOrderDetail = "INSERT INTO orderdetails(orderID, productID, quantity, discount) VALUES(?,?,?,?)";
	                    PreparedStatement pst3 = connection.prepareStatement(insertOrderDetail);
	                    pst3.setInt(1, orderId);
	                    pst3.setInt(2, productId);
	                    pst3.setInt(3, quantity);
	                    pst3.setBigDecimal(4, BigDecimal.ZERO);
	                    pst3.executeUpdate();

	                    // Update product stock
	                    String updateStock = "UPDATE products SET stockQuantity=stockQuantity-? WHERE productID=?";
	                    PreparedStatement pst4 = connection.prepareStatement(updateStock);
	                    pst4.setInt(1, quantity);
	                    pst4.setInt(2, productId);
	                    pst4.executeUpdate();

	                    connection.commit();   // Transaction success
	                    return true;
	                }
	            } else {
	                System.out.println("Insufficient Stock Available!");
	            }
	        } else {
	            System.out.println("Product Not Found!");
	        }
	    } catch (SQLException e) {
	        connection.rollback();  // Rollback if error
	        throw e;
	    } finally {
	        connection.setAutoCommit(true); // Reset AutoCommit
	    }
	    return false;
	}

	@Override
	public void getOrdersByCustomerId(int customerId) throws SQLException {
		String query = "SELECT o.OrderID,o.customerID,p.ProductName, o.TotalAmount AS Amount, o.OrderDate, o.Status FROM "
				+ "orders o JOIN orderDetails od ON o.OrderID = od.OrderID JOIN products p ON od.ProductID = p.ProductID  WHERE o.CustomerID = ?";

	try (PreparedStatement pst = connection.prepareStatement(query)) {
	    pst.setInt(1, customerId);

	    try (ResultSet rs = pst.executeQuery()) {
	        boolean found = false;

	        System.out.println("========================================");
	        System.out.println("              Your Orders               ");
	        System.out.println("========================================");

	        
	        System.out.println("Customer ID :" + customerId);
	        while (rs.next()) {
	            found = true;
	            System.out.println("Order ID      : " + rs.getInt("orderID"));
	            System.out.println("Product Name  : " + rs.getString("productName"));
	            System.out.println("Total Price   : " + rs.getBigDecimal("Amount"));
	            System.out.println("Order Date    : " + rs.getTimestamp("orderDate"));
	            System.out.println("Status    : " + rs.getString("Status"));
	            System.out.println("----------------------------------------");
	        }

	        if (!found) {
	            System.out.println("No Orders Found!");
	        }
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
}
}
