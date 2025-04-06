package com.techshop.entities;

import java.math.BigDecimal;

public class OrderDetails {
	// encapsulation
	private int orderDetailID;
	private Orders order; // Composition: OrderDetails "has-a" Order
	private Products product;// Composition: OrderDetails "has-a" Product
	private int quantity;
	private double discount;

	// construtors
	public OrderDetails() {

	}

	public OrderDetails(int orderDetailID, Orders order, Products product, int quantity) {
		this.orderDetailID = orderDetailID;
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.discount = 0; // Initialize discount to 0
	}

	// getters setters
	public int getOrderDetailID() {
		return orderDetailID;
	}

	public Orders getOrder() {
		return order;
	}

	public Products getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than 0.");
		}
		this.quantity = quantity;
	}

	public void setDiscount(double discount) {
		if (discount < 0d) {
			throw new IllegalArgumentException("Discount cannot be negative or null.");
		}
		this.discount = discount;
	}

	// CalculateSubtotal() - Calculate the subtotal for this order detail.
	public BigDecimal calculateSubtotal() {
		BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		return total.subtract(BigDecimal.valueOf(discount).min(total));// deduct the discount from total amount for an order
	}

	// GetOrderDetailInfo(): Retrieves and displays information about this order
	// detail.
	public void getOrderDetailInfo() {
		System.out.println(String.format(
				"OrderDetails ID:  %d , Order ID:  %d ,  Product:  %s  ,  Quantity: %d  , Discount : %s  , Subtotal:  %s",
				orderDetailID, order.getOrderID(), product.getProductName(), quantity, discount, calculateSubtotal()));
	}

	// UpdateQuantity(): Allows updating the quantity of the product in this order detail.
	public void updateQuantity(int newQuantity) {
		setQuantity(newQuantity);
	}

	// AddDiscount(): Applies a discount to this order detail.
	public void addDiscount(double discount) {
		
		setDiscount(discount);
	}

	public void setOrderDetailID(int id) {
		this.orderDetailID=id;
		
	}

	public void setOrder(Orders order) {
		this.order=order;
	}

	public void setProduct(Products product) {
		this.product=product;
		
	}
	
	@Override
	public String toString() {
	    return "OrderDetails [OrderID=" + order.getOrderID() + 
	           ", ProductID=" + product.getProductName() + 
	           ", Quantity=" + quantity + 
	           ", Price=" + product.getPrice() + "]";
	}

}
