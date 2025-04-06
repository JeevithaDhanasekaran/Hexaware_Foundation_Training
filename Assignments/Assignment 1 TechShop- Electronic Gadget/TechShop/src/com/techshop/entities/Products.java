package com.techshop.entities;

import java.math.BigDecimal;

/*
 Attributes:
• ProductID (int)
• ProductName (string)
• Description (string)
• Price (decimal)
 */

public class Products {
	
	//encapsulation
	//independent entity
	private int productID;
	private String productName;
	private String description;
	private BigDecimal price;
	private int stockQuantity;
	private String productLine;
	
	//constructors:
	public Products() {}
	
	public Products(int productID, String productName, String description, BigDecimal price, int stockQuantity,String productLine) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.productLine=productLine;
    }
	
	//getters setters
	public int getProductID() { 
		return productID; 
		}
    public String getProductName() { 
    	return productName;
    	}
    public String getDescription() { 
    	return description; 
    	}
    public BigDecimal getPrice() {
    	return price;
    	}
    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getProductLine() {
    	return productLine;
    }
    public void setProductLine(String productLine) {
    	this.productLine=productLine;
    }

    public void setDescription(String description) { 
    	this.description = description;
    	}
    public void setPrice(BigDecimal price) { 
    	if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
    	this.price = price; 
    }
    public void setStockQuantity(int stockQuantity) {
    	if(stockQuantity < 0) {
    		throw new IllegalArgumentException("Quantity should be positive");
    	}
        this.stockQuantity = stockQuantity;
    }
    
 // GetProductDetails(): Retrieves and displays detailed information about the product.
    public void getProductDetails() {
    	System.out.println(String.format("Product ID: %d, Name: %s, Description: %s, Price: %s, Stock: %d",
                productID, productName, description, price, stockQuantity));
    }

    // UpdateProductInfo(): Allows updates to product details (e.g., price, description).
    public void updateProductInfo(BigDecimal price, String description, int stockQuantity) {
        setPrice(price);
        setDescription(description);
        setStockQuantity(stockQuantity);
    }

    // IsProductInStock(): Checks if the product is currently in stock.
    public boolean isProductInStock() {
        return stockQuantity > 0;
    }

	public void setProductID(int productID) {
		
		this.productID=productID;
	}

	public void setProductName(String productName) {
		this.productName=productName;
		
	}
	
	public String toString() {
        return "Product ID: " + productID +
               ", Name: " + productName +
               ", Price: " + price +
               ", Quantity: " + stockQuantity;
    }
    
}
