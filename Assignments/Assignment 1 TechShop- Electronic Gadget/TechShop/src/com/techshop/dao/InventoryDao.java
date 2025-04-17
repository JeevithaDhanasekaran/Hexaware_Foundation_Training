package com.techshop.dao;

import java.sql.SQLException;
import java.util.List;

import com.techshop.entities.Inventory;

public interface InventoryDao {
	
	//add new row
	void addInventory(Inventory inventory) throws SQLException;
	//search by productID
    Inventory getInventoryByProductId(int productId) throws SQLException;
    //update quantity
    void updateStockQuantity(int productId, int newQuantity) throws SQLException;
    //check availability of product
    boolean isProductAvailable(int productId, int quantityToCheck) throws SQLException;
    //low stock products
    List<Inventory> listLowStockProducts(int threshold) throws SQLException;
    //out of stock products
    List<Inventory> listOutOfStockProducts() throws SQLException;
    //get inventory worth
    double getInventoryValue() throws SQLException;
	List<Inventory> getAllInventory() throws SQLException;
}
