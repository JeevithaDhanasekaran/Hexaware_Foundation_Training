package com.techshop.dao;

import java.util.List;

import com.techshop.entities.Products;

public interface ProductDao {
	//add product
	boolean addProduct(Products product) throws Exception;
	
	//get product details bu productID
	Products getProductById(int productId) throws Exception;
	
	//list all products
    List<Products> getAllProducts() throws Exception;
    
    //update product details
    boolean updateProduct(Products product) throws Exception;
    
    //delete a product with its productID
    boolean deleteProduct(int productId) throws Exception;
    
    //check if product is in stock
    boolean isProductInStock(int productId) throws Exception;
}
