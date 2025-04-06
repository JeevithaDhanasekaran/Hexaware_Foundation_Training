package com.techshop.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.techshop.dao.OrderDetailDao;
import com.techshop.entities.Customers;
import com.techshop.entities.OrderDetails;
import com.techshop.entities.Orders;
import com.techshop.entities.Products;
import com.techshop.exceptions.DatabaseConnectionException;
import com.techshop.util.DBConnUtil;

public class OrderDetailsDaoImpl implements OrderDetailDao{

	
	private Connection connection;

    public OrderDetailsDaoImpl() throws Exception {
    	try {
            this.connection = DBConnUtil.getConnection();
        } catch (Exception e) {
            throw new DatabaseConnectionException("Failed to connect to database: " + e.getMessage());
        }
    }
	
	@Override
	public void addOrderDetail(OrderDetails orderDetail) throws SQLException {
		String sql = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Discount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, orderDetail.getOrder().getOrderID());
            preparedStatement.setInt(2, orderDetail.getProduct().getProductID());
            preparedStatement.setInt(3, orderDetail.getQuantity());
            preparedStatement.setDouble(4, orderDetail.getDiscount());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("Failed to add order detail. No rows were inserted.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderDetail.setOrderDetailID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve generated OrderDetailID.");
                }
            }
        }
		
	}

	@Override
	public OrderDetails getOrderDetailById(int orderDetailId) throws SQLException {
		String sql = "SELECT od.*, o.CustomerID, o.OrderDate, o.TotalAmount, o.OrderStatus, " +
                "p.ProductName, p.Description, p.Price " +
                "FROM OrderDetails od " +
                "JOIN Orders o ON od.OrderID = o.OrderID " +
                "JOIN Products p ON od.ProductID = p.ProductID " +
                "WHERE od.OrderDetailID = ?";
   try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
       preparedStatement.setInt(1, orderDetailId);
       ResultSet resultSet = preparedStatement.executeQuery();
       if (resultSet.next()) {
           Orders order = new Orders();
           order.setOrderID(resultSet.getInt("OrderID"));
           // We might not need all customer details here, but the Order entity requires a Customer object.
           // You might fetch the full Customer object separately if needed.
           order.setCustomer(new Customers(resultSet.getInt("CustomerID"), null, null, null, null, null));
           order.setOrderDate(new Date(resultSet.getTimestamp("OrderDate").getTime()));
           order.setTotalAmount(resultSet.getBigDecimal("TotalAmount"));
           order.setOrderStatus(resultSet.getString("OrderStatus"));

           Products product = new Products();
           product.setProductID(resultSet.getInt("ProductID"));
           product.setProductName(resultSet.getString("ProductName"));
           product.setDescription(resultSet.getString("Description"));
           product.setPrice(resultSet.getBigDecimal("Price"));

           OrderDetails orderDetail = new OrderDetails();
           orderDetail.setOrderDetailID(resultSet.getInt("OrderDetailID"));
           orderDetail.setOrder(order);
           orderDetail.setProduct(product);
           orderDetail.setQuantity(resultSet.getInt("Quantity"));
           orderDetail.setDiscount(resultSet.getDouble("Discount"));
           return orderDetail;
       }
       else {
       throw new SQLException("OrderDetail with ID " + orderDetailId + " not found.");
       }
   }
	}

	@Override
	public List<OrderDetails> getOrderDetailsByOrderId(int orderId) throws SQLException {
		List<OrderDetails> orderDetailsList = new ArrayList<>();
        String sql = "SELECT od.*, p.ProductName, p.Description, p.Price " +
                     "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID " +
                     "WHERE od.OrderID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Products product = new Products();
                product.setProductID(resultSet.getInt("ProductID"));
                product.setProductName(resultSet.getString("ProductName"));
                product.setDescription(resultSet.getString("Description"));
                product.setPrice(resultSet.getBigDecimal("Price"));

                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setOrderDetailID(resultSet.getInt("OrderDetailID"));
                // We only have the OrderID here, you might need to fetch the full Order object if needed.
                orderDetail.setOrder(new Orders(resultSet.getInt("OrderID"), null, null, null, null, null));
                orderDetail.setProduct(product);
                orderDetail.setQuantity(resultSet.getInt("Quantity"));
                orderDetail.setDiscount(resultSet.getDouble("Discount"));
                orderDetailsList.add(orderDetail);
            }
        }
        return orderDetailsList;
	}

	@Override
	public List<OrderDetails> getAllOrderDetails() throws SQLException {
		List<OrderDetails> orderDetailsList = new ArrayList<>();
        String sql = "SELECT od.*, o.OrderID AS ParentOrderID, p.ProductID AS ChildProductID, p.ProductName, p.Description, p.Price " +
                     "FROM OrderDetails od " +
                     "JOIN Orders o ON od.OrderID = o.OrderID " +
                     "JOIN Products p ON od.ProductID = p.ProductID";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Orders order = new Orders();
                order.setOrderID(resultSet.getInt("ParentOrderID"));
                order.setCustomer(null); // You might need to fetch customer details separately
                order.setOrderDate(null); // You might need to fetch full order details separately
                order.setTotalAmount(null); // You might need to fetch full order details separately
                order.setOrderStatus(null); // You might need to fetch full order details separately

                Products product = new Products();
                product.setProductID(resultSet.getInt("ChildProductID"));
                product.setProductName(resultSet.getString("ProductName"));
                product.setDescription(resultSet.getString("Description"));
                product.setPrice(resultSet.getBigDecimal("Price"));

                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setOrderDetailID(resultSet.getInt("OrderDetailID"));
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(resultSet.getInt("Quantity"));
                orderDetail.setDiscount(resultSet.getDouble("Discount"));
                orderDetailsList.add(orderDetail);
            }
        }
        return orderDetailsList;
	}

	@Override
	public void updateOrderDetail(OrderDetails orderDetail) throws SQLException {
		
		String sql = "UPDATE OrderDetails SET Quantity = ?, Discount = ? WHERE OrderDetailID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderDetail.getQuantity());
            preparedStatement.setDouble(2, orderDetail.getDiscount());
            preparedStatement.setInt(3, orderDetail.getOrderDetailID());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("Failed to update order detail. No rows were updated.");
            }
        }
	}

	@Override
	public void deleteOrderDetail(int orderDetailId) throws SQLException {
		String sql = "DELETE FROM OrderDetails WHERE OrderDetailID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderDetailId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("Failed to delete order detail. No rows were deleted.");
            }
        }
		
	}

	@Override
	public void addDiscount(int orderDetailId, double discount) throws SQLException {
		String sql = "UPDATE OrderDetails SET Discount = ? WHERE OrderDetailID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, discount);
            preparedStatement.setInt(2, orderDetailId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("Failed to add discount to order detail. No rows were updated.");
            }
        }
		
	}
	
}
