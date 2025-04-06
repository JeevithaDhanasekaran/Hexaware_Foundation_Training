package com.techshop.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
Attributes:
• OrderID (int)
• Customer (Customer) - Use composition to reference the Customer who placed the order.
• OrderDate (DateTime)
• TotalAmount (decimal)
*/

public class Orders {
	
	//encapsulation
	private int orderID;
	private Date orderDate;
	private BigDecimal totalAmount;
	private Customers customer;//aggregation orders-has-a-relaationship with customers
	private String orderStatus; // Added order status
    private List<OrderDetails> orderDetails; // Added list of OrderDetails
	
	//constructors
	public Orders() {
	
	}

	public Orders(int orderID, Customers customer, Date orderDate, BigDecimal totalAmount, String orderStatus, List<OrderDetails> orderDetails) {
        this.orderID = orderID;
        this.customer = customer;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
    }

	
	//getters setters
	public int getOrderID() {
		return orderID;
	}
	
	public Customers getCustomer() {
		return customer;
	}
	
	public Date getOrderDate() {
		return orderDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	
	public String getOrderStatus() {
        return orderStatus;
	}
	
	public void setOrderID(int orderID) {
		this.orderID=orderID;
	}
	
	public void setCustomer(Customers customer) {
		this.customer=customer;
	}
	
	//check if the string is not empty
	public void setOrderStatus(String orderStatus) {
		if (orderStatus == null || orderStatus.trim().isEmpty()) {
	        throw new IllegalArgumentException("Order status cannot be empty or null.");
	    }
        this.orderStatus = orderStatus;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }
//check it's not null
    public void setOrderDetails(List<OrderDetails> orderDetails) {
    	if (orderDetails == null) {
            throw new IllegalArgumentException("Order details list cannot be null.");
        }
    	this.orderDetails = orderDetails;
    }

    // CalculateTotalAmount() - Calculate the total amount of the order.
    public BigDecimal calculateTotalAmount() {
    	if (orderDetails == null || orderDetails.isEmpty()) {
            return BigDecimal.ZERO; // Return 0 if orderDetails is null or empty
        }

        return orderDetails.stream()
                .map(detail -> detail.getProduct().getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())).subtract(BigDecimal.valueOf(detail.getDiscount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // GetOrderDetails(): Retrieves and displays the details of the order (e.g., product list and quantities).
    public void getOrderDetailsInfo() {
        System.out.println("Order ID: " + orderID);
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Order Date: " + orderDate);
        System.out.println("Order Status: " + orderStatus);
        System.out.println("Order Details:");
        if (orderDetails != null) { //check if it is not null
            for (OrderDetails detail : orderDetails) {
            	System.out.println(String.format(
                        "  Product: %s, Quantity: %d, Price: %s",
                        detail.getProduct().getProductName(),
                        detail.getQuantity(),
                        detail.getProduct().getPrice()
                ));
            }
        }
        System.out.println("Total Amount: " + totalAmount);
    }

    // UpdateOrderStatus(): Allows updating the status of the order (e.g., processing, shipped).
    public void updateOrderStatus(String newStatus) {
    	if (newStatus == null || newStatus.trim().isEmpty()) {
    		throw new IllegalArgumentException("Order status cannot be empty or null.");
    	}
    	this.orderStatus = newStatus.trim();
    }

    // CancelOrder(): Cancels the order and adjusts stock levels for products.
    public void cancelOrder(List<Products> allProducts) {
    	if ("Cancelled".equalsIgnoreCase(orderStatus)) {
            System.out.println("Order is already cancelled");
            return;
        }
        setOrderStatus( "Cancelled");
        if (orderDetails != null) {
            for (OrderDetails detail : orderDetails) {
                Products product = detail.getProduct();
                int quantity = detail.getQuantity();

                for (Products existingProduct : allProducts) {
                    if (existingProduct.getProductID() == product.getProductID()) {
                        int currentStock = existingProduct.getStockQuantity();
                        existingProduct.setStockQuantity(currentStock + quantity);
                        break;
                    }//end if
                }//end inner for
            }//end outer for
        }//end outer if
    }

	public void setTotalAmount(BigDecimal bigDecimal) {
		if(bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
		    throw new IllegalArgumentException("Amount must be greater than zero");
		}
		this.totalAmount = bigDecimal;
		
	}

	public void setOrderDate(Date orderDate) {
		if(orderDate == null) {
	        throw new IllegalArgumentException("Order date cannot be null");
	    }
	    this.orderDate  = orderDate;
		
	}
	
	public String toString() {
	    return "Order [OrderID=" + orderID + 
	           ", Customer=" + customer.getFirstName() + 
	           customer.getLastName()	+
				", OrderDate=" + orderDate + 
	           ", TotalAmount=" + totalAmount + "]";
	}
	
}