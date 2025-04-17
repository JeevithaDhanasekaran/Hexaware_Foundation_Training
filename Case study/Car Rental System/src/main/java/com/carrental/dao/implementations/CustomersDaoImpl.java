package com.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.carrental.dao.CustomersDao;
import com.carrental.entity.Customers;
import java.sql.Statement;

import com.carrental.exception.CustomerNotFoundException;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public class CustomersDaoImpl implements CustomersDao {

	private Connection connection;

	public CustomersDaoImpl(Connection connection) throws DatabaseConnectionException {
		if (connection == null) {
			throw new DatabaseConnectionException("Failed to connect to database");
		}
		this.connection = connection;
	}
	
	private Customers mapToCustomers(ResultSet rs) throws SQLException, InvalidInputException {
		Customers customer = new Customers();
		customer.setCustomerId(rs.getInt("customerId"));
		customer.setFirstName(rs.getString("firstName"));
		customer.setLastName(rs.getString("lastName"));
		customer.setLicenseNumber(rs.getString("licenceNumber"));
		return customer;
	}

	@Override
	public boolean addCustomer(Customers customer) throws DatabaseConnectionException, CustomerNotFoundException {
		String sql = "INSERT INTO customers (firstName, lastName, licenceNumber) VALUES (?, ?, ?)";

		if(customer == null) {
			throw new CustomerNotFoundException("Object cannot be null. ID needs to passed");
		}
		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getLicenseNumber());
			int rows = ps.executeUpdate();

			if (rows <= 0) {
				throw new DatabaseConnectionException("No rows Updated");
			}

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					customer.setCustomerId(rs.getInt(1)); // Auto-generated key
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();//only for debugging
			System.err.println("Error adding customer " );
			return false;
		}

		return true;
	}

	@Override
	public Customers findById(int customerId) throws InvalidInputException {
		String sql = "SELECT * FROM customers WHERE customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) 
				return mapToCustomers(rs);
			else
				throw new RuntimeException("No elements retrieved");
		} catch (SQLException e) {
			e.printStackTrace();//only for debugging
			System.out.println("Error finding customer: " + e.getMessage());
		}
		return null;
	}

	@Override
	public String getLicenseNumberByCustomerId(int customerId) {
		String sql = "SELECT licenceNumber FROM customers WHERE customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {
	            ps.setInt(1, customerId);
	            if (rs.next()) {
	                return rs.getString("licenseNumber");
	            }
	        } catch (SQLException e) {
	        	e.printStackTrace();//for debugging
	            System.out.println("Error getting license number: " + e.getMessage());
	        }
	        return null;
	}

	@Override
	public List<Customers> findAllCustomers() throws InvalidInputException {
		List<Customers> customers = new ArrayList<>();
		String sql = "SELECT customerID, firstName, lastName, licenceNumber FROM customers";
		try (PreparedStatement ps = connection.prepareStatement(sql);
		     ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				customers.add(mapToCustomers(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error fetching all customers: " + e.getMessage());
		}
		return customers;
	}

	@Override
	public boolean updateCustomer(Customers customer) {
		String sql = "UPDATE customers SET firstName=?, lastName=?, licenceNumber=?  WHERE customerID=?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getLicenseNumber());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error updating customer: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		String sql = "DELETE FROM customers WHERE customer_id=?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, customerId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error deleting customer: ");
			return false;
		}
	}

	@Override
	public Customers addCustomer2(Customers customer) {
		String sql = "INSERT INTO Customers (firstName, lastName, licenceNumber) VALUES (?, ?, ?)";
	    try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setString(1, customer.getFirstName());
	        ps.setString(2, customer.getLastName());
	        ps.setString(3, customer.getLicenseNumber());

	        int rows = ps.executeUpdate();

	        if (rows == 0) {
	            throw new SQLException("Failed to insert customer, no rows affected.");
	        }

	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                customer.setCustomerId(rs.getInt(1));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	    return customer;
	}

	@Override
	public int getCustomerIdByLicense(String licenseNumber) throws DatabaseConnectionException {
		String query = "SELECT customerID FROM Customers WHERE licenseNumber = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        
	        stmt.setString(1, licenseNumber);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("customerID");
	        }
	    } catch (SQLException e) {
	        throw new DatabaseConnectionException("Error fetching customer ID by license: " + e.getMessage());
	    }
	    return -1; // not found
	}

}
