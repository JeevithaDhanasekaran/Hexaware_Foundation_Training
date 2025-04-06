package com.techshop.dao;

import java.sql.SQLException;
import java.util.List;

import com.techshop.entities.Orders;
import com.techshop.exceptions.OrderProcessingException;

public interface OrderDao {
	//add order
	void addOrder(Orders order) throws SQLException, OrderProcessingException;
	//get by orderID
    Orders getOrderById(int orderId) throws SQLException;
    //get all list of orders
    List<Orders> getAllOrders() throws SQLException;
    //update the status of orders
    void updateOrderStatus(int orderId, String status) throws SQLException;
    //delete orders
    void cancelOrder(int orderId) throws SQLException;
    //total amount calculation
    double calculateTotalAmount(int orderId) throws SQLException;
    //method to place order
	boolean placeOrder(int customerId, int productId, int quantity) throws SQLException;
	void getOrdersByCustomerId(int customerId) throws SQLException;
}
