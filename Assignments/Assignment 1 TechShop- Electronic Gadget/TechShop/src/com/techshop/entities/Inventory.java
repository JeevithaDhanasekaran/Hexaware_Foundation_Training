package com.techshop.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
	
	//encapsulation
	private int inventoryID;
	private Products product;  // Composition: Inventory "has-a" Product
	private int quantityInStock;
	private Date lastStockUpdate;

	
	//parameterized constructor
	
	public Inventory(int inventoryID, Products product, int quantityInStock,Date lastUpdated) {
		this.inventoryID = inventoryID;
		this.product = product;
		this.quantityInStock = quantityInStock;
		this.lastStockUpdate = new Date();  // current date-time
	}
	
	public Inventory() {
	}
	

	//getters setters
	public int getInventoryID() { 
		return inventoryID;
		}
    public Date getLastStockUpdate() { 
    	return lastStockUpdate;
    	}
    public void setQuantityInStock( int  quantityInStock) { 
    	if (quantityInStock < 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
    	this.quantityInStock= quantityInStock;
    }
    
//    GetProduct(): A method to retrieve the product associated with this inventory item.
    
    public Products getProduct() { 
    	return product; 
    	}
    
//    • GetQuantityInStock(): A method to get the current quantity of the product in stock.
    public int getQuantityInStock() { 
    	return quantityInStock;
    	}

//    • AddToInventory(int quantity): A method to add a specified quantity of the product to the 
//    inventory.
    public void addToInventory(int quantity) { 
    	if (quantityInStock < 0) {
    		throw new IllegalArgumentException("Quantity cannot be negative.");
    	}
    	setQuantityInStock(this.quantityInStock + quantity);
    	this.lastStockUpdate = new Date();
    	}

//    • RemoveFromInventory(int quantity): A method to remove a specified quantity of the product 
//    from the inventory.
    
    public void removeFromInventory(int quantity) { 
    	if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to remove must be positive.");
        }
        if (this.quantityInStock < quantity) {
            throw new IllegalArgumentException("Insufficient stock to remove.");
        }
    	setQuantityInStock(this.quantityInStock - quantity);
    	this.lastStockUpdate = new Date();
    	}
    
//    • UpdateStockQuantity(int newQuantity): A method to update the stock quantity to a new value.
    
    public void updateStockQuantity(int newQuantity) {
    	if (quantityInStock < 0) {
    		throw new IllegalArgumentException("Quantity cannot be negative.");
    	}
        setQuantityInStock(newQuantity);
        this.lastStockUpdate = new Date();
    }
    
//    • IsProductAvailable(int quantityToCheck): A method to check if a specified quantity of the 
//    product is available in the inventory.
    
    public boolean isProductAvailable(int quantity) {
        return this.quantityInStock >= quantity;
    }
    
//    • GetInventoryValue(): A method to calculate the total value of the products in the inventory 
//    based on their prices and quantities.
    
    public BigDecimal getInventoryValue() {
    	return product.getPrice().multiply(BigDecimal.valueOf(quantityInStock));
    }
    
//    • ListLowStockProducts(int threshold): A method to list products with quantities below a specified 
//    threshold, indicating low stock.
    
    public static List<Inventory> listLowStockProducts(List<Inventory> inventoryList, int threshold) {
    	if (inventoryList == null) {
            return new ArrayList<>(); // Return an empty list for null input
        }

        return inventoryList.stream()
                .filter(inventory -> inventory.getQuantityInStock() < threshold)
                .collect(Collectors.toList());
    }
    
//    • ListOutOfStockProducts(): A method to list products that are out of stock.
    
    public static List<Inventory> listOutOfStockProducts(List<Inventory> inventoryList) {
        return listLowStockProducts(inventoryList, 1); // Products with quantity less than 1 are out of stock
    }
    
//    • ListAllProducts(): A method to list all products in the inventory, along with their quantities.
    
    public static void listAllProducts(List<Inventory> inventoryList) {
        if (inventoryList == null || inventoryList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("Inventory List:");
        for (Inventory inventory : inventoryList) {
            System.out.println(String.format(
                    "Product: %s, Quantity: %d",
                    inventory.getProduct().getProductName(),
                    inventory.getQuantityInStock()
            ));
        }
    }

	public void setInventoryID(int inventoryID) {
		this.inventoryID=inventoryID;
		
	}

	public void setProduct(Products product) {
		this.product=product;
		
	}

	public void setLastStockUpdate(java.sql.Date date) {
		this.lastStockUpdate=date;
	}

	@Override
	public String toString() {
	    return "Inventory [ProductID=" + product.getProductID() + 
	    		", Product Name= " + product.getProductName() +
	           ", Available Quantity=" + quantityInStock + "]";
	}
	
}
