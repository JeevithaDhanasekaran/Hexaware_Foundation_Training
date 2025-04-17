package com.techshop.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.techshop.dao.CustomerDao;
import com.techshop.entities.Customers;
import com.techshop.exceptions.CustomerNotFoundException;
import com.techshop.exceptions.DatabaseConnectionException;
import com.techshop.util.DBConnUtil;

public class CustomerDaoImpl implements CustomerDao {

	private Connection conn;
	
	public CustomerDaoImpl() throws DatabaseConnectionException {
        try {
            this.conn = DBConnUtil.getConnection();
        } catch (Exception e) {
            throw new DatabaseConnectionException("Failed to connect to database: " + e.getMessage());
        }
    }
	
	@Override
	public boolean addCustomer(Customers customer) throws DatabaseConnectionException {
		String query="INSERT INTO CUSTOMERS (customerID,firstName,lastName,email,phone,address) VALUES (?,?,?,?,?,?)";
		try(  PreparedStatement stmt=conn.prepareStatement(query)  ){
			
			stmt.setInt(1, customer.getCustomerID());
			stmt.setString(2, customer.getFirstName());
			stmt.setString(3, customer.getLastName());
			stmt.setString(4, customer.getEmail());
			stmt.setString(5, customer.getPhoneNumber());
			stmt.setString(6, customer.getAddress());
			
			int rowsAffected=stmt.executeUpdate();
			if (rowsAffected == 0) {
			    throw new DatabaseConnectionException("Failed to insert customer.");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
		
	}

	@Override
	public Customers getCustomerById(int customerID) throws DatabaseConnectionException, CustomerNotFoundException {
		Customers customer=null;
		String query="SELECT * FROM CUSTOMERS WHERE customerID=?";
		try(PreparedStatement stmt=conn.prepareStatement(query)) {
			
			stmt.setInt(1, customerID);
			try(ResultSet rs=stmt.executeQuery()){
				
				if (rs.next()) {
		            customer = new Customers(
		                rs.getInt("customer_id"),
		                rs.getString("firstName"),
		                rs.getString("lastName"),
		                rs.getString("email"),
		                rs.getString("phone"),
		                rs.getString("address")
		            );
				}
				 else {
		                throw new CustomerNotFoundException("Customer with ID " + customerID + " not found.");
		            }
			}
		}
		catch (SQLException e) {
		    throw new DatabaseConnectionException("Database error: " + e.getMessage());
		} catch (CustomerNotFoundException e) {
		    throw e;
		}
		return customer;
	}


	@Override
	public List<Customers> listAllCustomer() throws DatabaseConnectionException {
		List<Customers> customers=new ArrayList<>();
		String query="SELECT * FROM Customers";
		try(Statement st=conn.createStatement()){
			ResultSet rs=st.executeQuery(query);
			
			while(rs.next()) {
				customers.add(
						new Customers(
								rs.getInt("CustomerID"),
								rs.getString("firstName"),
		                        rs.getString("lastName"),
		                        rs.getString("email"),
		                        rs.getString("Phone"),
		                        rs.getString("address")
								)
						);
			}
		}
		catch(SQLException e) {
			throw new DatabaseConnectionException("Database error: " + e.getMessage());
		}
		return customers;
	}

	@Override
	public void updateCustomer(Customers customer) throws DatabaseConnectionException {
		String query = "UPDATE customers SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, Address = ? WHERE CustomerID = ?";
        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getCustomerID());
           
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("No customer was updated.");
            }
            
        } catch (SQLException e) {
        	throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
		
	}

	@Override
	public void deleteCustomer(int customerID) throws DatabaseConnectionException {
		String sql = "DELETE FROM Customers WHERE customerID = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerID);
            int rows=preparedStatement.executeUpdate();
            if (rows == 0) {
                System.err.println("No customer deleted.");
            }
        } catch (SQLException e) {
        	throw new DatabaseConnectionException("Database error: " + e.getMessage());
        }
	}

	@Override
	public int getTotalOrdersByCustomer(int customerId) throws CustomerNotFoundException, DatabaseConnectionException {
	    int totalOrders = 0;

	    try (
	         PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE customer_id = ?")) {

	        ps.setInt(1, customerId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            totalOrders = rs.getInt(1);
	        } else {
	            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
	        }

	    } catch (SQLException e) {
	        throw new DatabaseConnectionException("Database error: " + e.getMessage());
	    }

	    return totalOrders;
	}
	
}