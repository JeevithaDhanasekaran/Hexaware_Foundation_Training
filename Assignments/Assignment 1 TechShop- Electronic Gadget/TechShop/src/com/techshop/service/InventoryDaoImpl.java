package com.techshop.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.techshop.dao.InventoryDao;
import com.techshop.entities.Inventory;
import com.techshop.entities.Products;
import com.techshop.exceptions.DatabaseConnectionException;
import com.techshop.util.DBConnUtil;

public class InventoryDaoImpl implements InventoryDao{

	private Connection connection;

    public InventoryDaoImpl() throws Exception {
    	try {
            this.connection = DBConnUtil.getConnection();
        } catch (Exception e) {
            throw new DatabaseConnectionException("Failed to connect to database: " + e.getMessage());
        }
    }
	
	@Override
	public void addInventory(Inventory inventory) throws SQLException {
		String sql = "INSERT INTO Inventory (ProductID, QuantityInStock) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, inventory.getProduct().getProductID());
            preparedStatement.setInt(2, inventory.getQuantityInStock());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("Failed to add inventory. No rows were inserted.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inventory.setInventoryID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve generated InventoryID.");
                }
            }
        }
	}

	@Override
	public Inventory getInventoryByProductId(int productId) throws SQLException {
		String sql = "SELECT i.*, p.ProductName, p.Description, p.Price " +
                "FROM Inventory i JOIN Products p ON i.ProductID = p.ProductID " +
                "WHERE i.ProductID = ?";
   try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
       preparedStatement.setInt(1, productId);
       ResultSet resultSet = preparedStatement.executeQuery();
       if (resultSet.next()) {
           Products product = new Products(
                   resultSet.getInt("ProductID"),
                   resultSet.getString("ProductName"),
                   resultSet.getString("Description"),
                   resultSet.getBigDecimal("Price"),
                   -1,
                   resultSet.getString("productline")
           );

           Inventory inventory = new Inventory(
                   resultSet.getInt("InventoryID"),
                   product,
                   resultSet.getInt("QuantityInStock"),
                   new Date(resultSet.getTimestamp("LastStockUpdate").getTime())
           );
           return inventory;
       }
   }
   return null;
	}

	@Override
	public void updateStockQuantity(int productId, int newQuantity) throws SQLException {
		String sql = "UPDATE Inventory SET QuantityInStock = ?, LastStockUpdate = CURRENT_TIMESTAMP WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("Failed to update stock quantity. No rows were updated.");
            }
        }
		
	}

	@Override
	public boolean isProductAvailable(int productId, int quantityToCheck) throws SQLException {
		Inventory inventory = getInventoryByProductId(productId);
        return inventory != null && inventory.isProductAvailable(quantityToCheck);
	}

	@Override
	public List<Inventory> listLowStockProducts(int threshold) throws SQLException {
		List<Inventory> lowStockList = new ArrayList<>();
        String sql = "SELECT i.*, p.ProductName, p.Description, p.Price " +
                     "FROM Inventory i JOIN Products p ON i.ProductID = p.ProductID " +
                     "WHERE i.QuantityInStock > 0 AND i.QuantityInStock <= ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("Description"),
                            rs.getBigDecimal("Price"),
                            -1,
                            rs.getString("productline")
                    );
                    Inventory inventory = new Inventory(
                            rs.getInt("InventoryID"),
                            product,
                            rs.getInt("QuantityInStock"),
                            rs.getTimestamp("LastStockUpdate")
                    );
                    lowStockList.add(inventory);
                }
            }
        }
        return lowStockList;
	}

	@Override
	public List<Inventory> listOutOfStockProducts() throws SQLException {
		List<Inventory> outOfStockList = new ArrayList<>();
        String sql = "SELECT i.*, p.ProductName, p.Description, p.Price " +
                     "FROM Inventory i JOIN Products p ON i.ProductID = p.ProductID " +
                     "WHERE i.QuantityInStock = 0";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("Description"),
                        rs.getBigDecimal("Price"),
                        -1,
                        rs.getString("productline")
                );
                Inventory inventory = new Inventory(
                        rs.getInt("InventoryID"),
                        product,
                        rs.getInt("QuantityInStock"),
                        rs.getTimestamp("LastStockUpdate")
                );
                outOfStockList.add(inventory);
            }
        }
        return outOfStockList;
	}

	@Override
	public double getInventoryValue() throws SQLException {
		String sql = "SELECT SUM(i.QuantityInStock * p.Price) AS TotalValue " +
                "FROM Inventory i JOIN Products p ON i.ProductID = p.ProductID";
   try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)) {
       if (resultSet.next()) {
           return resultSet.getDouble("TotalValue");
       }
   }
   return 0.0;
	}

	@Override
	public List<Inventory> getAllInventory() throws SQLException {
		List<Inventory> allInventory = new ArrayList<>();
        String sql = "SELECT i.*, p.ProductName, p.Description, p.Price " +
                     "FROM Inventory i JOIN Products p ON i.ProductID = p.ProductID";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Products product = new Products(
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("Description"),
                        resultSet.getBigDecimal("Price"),
                        -1, // Stock quantity not directly from Products table here
                        resultSet.getString("productline")
                );

                Inventory inventory = new Inventory(
                        resultSet.getInt("InventoryID"),
                        product,
                        resultSet.getInt("QuantityInStock"),
                        new Date(resultSet.getTimestamp("LastStockUpdate").getTime())
                );
                allInventory.add(inventory);
            }
        }
        return allInventory;
    }
}

