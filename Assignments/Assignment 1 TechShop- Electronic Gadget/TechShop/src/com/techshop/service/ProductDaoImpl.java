package com.techshop.service;

import com.techshop.dao.ProductDao;
import com.techshop.entities.Products;
import com.techshop.util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
	private Products mapResultSetToProduct(ResultSet rs) throws SQLException {
		return new Products(
				rs.getInt("productId"),
				rs.getString("productName"),
				rs.getString("description"),
				rs.getBigDecimal("price"),
				rs.getInt("stockQuantity"),
				rs.getString("productline")   
			);
    }

    private void setProductParams(PreparedStatement ps, Products product) throws SQLException {
        ps.setString(1, product.getProductName());
        ps.setString(2, product.getDescription());
        ps.setBigDecimal(3, product.getPrice());
        ps.setString(4, product.getProductLine());
        ps.setInt(5, product.getStockQuantity());
    }

    @Override
    public boolean addProduct(Products product) throws Exception {
        String sql = "INSERT INTO Products (productName, description, price,productline,stockQuantity) VALUES (?, ?, ?, ?,?)";
        boolean r=true;
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setProductParams(ps, product);

            if (ps.executeUpdate() == 0) {
            	r=false;
                throw new SQLException("Failed to add product, no rows affected.");
            }

        } catch (SQLException e) {
        	r=false;
            throw new Exception("Error in addProduct: " + e.getMessage(), e);
        }
		return r;
    }

    @Override
    public Products getProductById(int productId) throws Exception {
        String sql = "SELECT * FROM Products WHERE productId = ?";

        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error in getProductById: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Products> getAllProducts() throws Exception {
        String sql = "SELECT * FROM Products";
        List<Products> products = new ArrayList<>();

        try (Connection conn = DBConnUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            throw new Exception("Error in getAllProducts: " + e.getMessage(), e);
        }

        return products;
    }

    @Override
    public boolean updateProduct(Products product) throws Exception {
        String sql = "UPDATE products SET productName = ?, description = ?,  price = ?, productline= ?, stockQuantity = ?   WHERE productID = ?";
        boolean ret=true;
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setProductParams(ps, product);
            ps.setInt(6, product.getProductID());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Failed to update product, no rows affected.");
            }

        } catch (SQLException e) {
            throw new Exception("Error in updateProduct: " + e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public boolean deleteProduct(int productId) throws Exception {
        String sql = "DELETE FROM Products WHERE productId = ?";

        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Failed to delete product, no rows affected.");
            }

        } catch (SQLException e) {
            throw new Exception("Error in deleteProduct: " + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public boolean isProductInStock(int productId) throws Exception {
        String sql = "SELECT stockQuantity FROM Products WHERE productId = ?";

        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("stockQuantity") > 0;
            }

        } catch (SQLException e) {
            throw new Exception("Error in isProductInStock: " + e.getMessage(), e);
        }
    }
}
