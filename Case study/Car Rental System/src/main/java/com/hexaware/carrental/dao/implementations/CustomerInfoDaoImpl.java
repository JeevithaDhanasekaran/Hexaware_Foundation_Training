package com.hexaware.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.CustomerInfoDao;
import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public class CustomerInfoDaoImpl implements CustomerInfoDao{

	private Connection connection;

	public CustomerInfoDaoImpl(Connection connection) throws DatabaseConnectionException {
		if (connection == null) {
			throw new DatabaseConnectionException("Failed to connect to database");
		}
		this.connection = connection;
	}
	
	private CustomerInfo mapToCustomerInfo(ResultSet rs) throws SQLException, InvalidInputException {
		CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setContactId(rs.getInt("contactID"));
        
        Customers customer = new Customers();
        customer.setCustomerId(rs.getInt("customerID"));
        
        customerInfo.setCustomers(customer);
        customerInfo.setEmail(rs.getString("email").toLowerCase().trim());
        customerInfo.setPhoneNumber(rs.getString("phoneNumber"));
        customerInfo.setAddress(rs.getString("address"));
        customerInfo.setRole(rs.getString("role"));
        return customerInfo;
	}

	
	
	
	@Override
	public boolean addCustomerInfo(CustomerInfo customerInfo) {
		String sql = "INSERT INTO ContactInfo (customerId, email, phoneNumber, address, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerInfo.getCustomers().getCustomerId());
            ps.setString(2, customerInfo.getEmail());
            ps.setString(3, customerInfo.getPhoneNumber());
            ps.setString(4, customerInfo.getAddress());
            ps.setString(5, customerInfo.getRole().toString());

            int rowsAffected = ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customerInfo.setContactId(generatedKeys.getInt(1));
                }
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding CustomerInfo");
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public CustomerInfo findByCustomerId(int customerId) throws InvalidInputException {
		String sql = "SELECT contactID,customerID, email, phoneNumber, address, role FROM contactInfo WHERE customerID = ?"; 
        try (PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setInt(1, customerId);
            
            ResultSet rs = ps.executeQuery();
            		
            if (rs.next()) {
                return mapToCustomerInfo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding CustomerInfo for customer ID " + customerId );
             e.printStackTrace();
        }
        return null;
	}

	


	@Override
	public List<CustomerInfo> findAll() throws InvalidInputException {
		List<CustomerInfo> customerInfos = new ArrayList<>();
        String sql = "SELECT contactID, customerID, email, phoneNumber, address, role FROM ContactInfo"; // Select only necessary columns
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                customerInfos.add(mapToCustomerInfo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all CustomerInfo: " + e.getMessage());
            e.printStackTrace();
        }
        return customerInfos;
	}

	@Override
	public boolean updateCustomerInfo(CustomerInfo customerInfo) {
		String sql = "UPDATE contactInfo SET email = ?, phoneNumber = ?, address = ?  WHERE contactID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, customerInfo.getEmail());
			ps.setString(2, customerInfo.getPhoneNumber());
			ps.setString(3, customerInfo.getAddress());
			ps.setInt(4, customerInfo.getContactId());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error updating CustomerInfo ");
		}
		return false;
	}

	@Override
	public boolean deleteByContactId(int contactId) {
		String sql = "DELETE FROM contactInfo WHERE contactID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, contactId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error deleting CustomerInfo: " + e.getMessage());
		}
		return false;
	}

	@Override
	public CustomerInfo findByContactId(int contactId) throws InvalidInputException {
        String sql = "SELECT contactID, customerID, email, phoneNumber, address, role FROM ContactInfo WHERE contactID=? "; // Select only necessary columns
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapToCustomerInfo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all CustomerInfo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public boolean deleteByCustomerId(int customerId) {
		String sql = "DELETE FROM contactInfo WHERE customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, customerId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error deleting CustomerInfo: " + e.getMessage());
		}
		return false;
	}

	@Override
	public CustomerInfo findByPhone(String phone) {
		String sql = "SELECT * FROM ContactInfo WHERE phoneNumber=?";
	    try (PreparedStatement pst = connection.prepareStatement(sql)) {
	        pst.setString(1, phone);
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            return mapToCustomerInfo(rs);
	        }
	    } catch (SQLException | InvalidInputException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
