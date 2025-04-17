package com.techshop.dao;

import java.sql.SQLException;
import java.util.List;

import com.techshop.entities.OrderDetails;

public interface OrderDetailDao {
	
	// add order details
	void addOrderDetail(OrderDetails orderDetail) throws SQLException;
	//get record based on orderDetailsID
    OrderDetails getOrderDetailById(int orderDetailId) throws SQLException;
    //get records based on orderID
    List<OrderDetails> getOrderDetailsByOrderId(int orderId) throws SQLException;
    //get all the data
    List<OrderDetails> getAllOrderDetails() throws SQLException;
    //update records
    void updateOrderDetail(OrderDetails orderDetail) throws SQLException;
    //delete records
    void deleteOrderDetail(int orderDetailId) throws SQLException;
    //add discount
    void addDiscount(int orderDetailId, double discount) throws SQLException;
}
