package com.hexaware.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.AuthDao;
import com.hexaware.carrental.entity.Auth;
import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public class AuthDaoImpl implements AuthDao {

	private Connection connection;

	public AuthDaoImpl(Connection connection) {
		this.connection = connection;
	}

	//implement Bcrypt to implement encrption of password data in future
	
	// reusable code to map Auth
	private Auth mapToAuth(ResultSet rs) throws SQLException {
	    Auth auth = new Auth();
	    auth.setAuthId(rs.getInt("authID"));

	    Customers customer = new Customers();
	    customer.setCustomerId(rs.getInt("customerID"));

	    auth.setCustomers(customer);
	    try {
			auth.setUserName(rs.getString("username"));
			auth.setPassword(rs.getString("password"));
		} catch (InvalidInputException | SQLException e) {
			System.out.println("Auth Dao implementation");
			e.printStackTrace();
		}
	    return auth;
	}


	@Override
	public boolean addAuth(Auth auth) throws DatabaseConnectionException {
		String sql = "INSERT INTO Auth (customerID, username, password) VALUES (?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, auth.getCustomers().getCustomerId());
			ps.setString(2, auth.getUserName());
			ps.setString(3, auth.getPassword());

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Failed to add Auth");
			}

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					auth.setAuthId(rs.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error adding Auth: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateAuth(Auth auth) throws DatabaseConnectionException {
		
		String sql = "UPDATE Auth SET username = ?, password = ? WHERE customerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, auth.getUserName());
            ps.setString(2, auth.getPassword());
            ps.setInt(3, auth.getCustomers().getCustomerId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error updating Auth for customer ID " + auth.getCustomers().getCustomerId() + ": " + e.getMessage());
        }
        return false;

	}

	@Override
	public boolean getAuthById(int authId) throws DatabaseConnectionException {
		String sql = "DELETE FROM Auth WHERE customerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, authId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error deleting Auth for customer ID " + authId + ": " + e.getMessage());
        }
        return false;
	}

	@Override
	public Auth getAuthByCustomerId(int customerId) throws DatabaseConnectionException {
		String sql = "SELECT a.authID, a.username, a.password, c.customerID "
				+ "FROM Auth a JOIN Customers c ON a.customerID = c.customerID "
				+ "WHERE a.customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, customerId);
			
			try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return mapToAuth(rs);
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error finding Auth by customer ID " + customerId + ": " + e.getMessage());
		}
		return null;
	}

	@Override
	public Auth getAuthByUsername(String username) throws DatabaseConnectionException {
		String sql = "SELECT a.authID, a.customerID, a.password FROM Auth a WHERE a.username = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			ps.setString(1, username);
			if (rs.next()) {
				Auth auth = new Auth();
				auth.setAuthId(rs.getInt("authID"));
				Customers customer = new Customers();
				customer.setCustomerId(rs.getInt("customerID"));
				auth.setCustomers(customer);
				try {
					auth.setUserName(username);
					auth.setPassword(rs.getString("password"));
				} catch (InvalidInputException e) {
					System.err.println("Error setting username/password during mapping: " + e.getMessage());
					e.printStackTrace();
				}
				return auth;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error finding Auth by username " + username + ": " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Auth> getAllAuths() throws DatabaseConnectionException {
		List<Auth> authList = new ArrayList<>();
        String sql = "SELECT a.authID, a.customerID, a.username, a.password FROM Auth a";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Auth auth = new Auth();
                auth.setAuthId(rs.getInt("authID"));
                Customers customer = new Customers();
                customer.setCustomerId(rs.getInt("customerID"));
                auth.setCustomers(customer);
                try {
                    auth.setUserName(rs.getString("username"));
                    auth.setPassword(rs.getString("password"));
                } catch (InvalidInputException e) {
                    System.err.println("Error setting username/password during mapping: " + e.getMessage());
                    e.printStackTrace();
                }
                authList.add(auth);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error fetching all Auth records: " + e.getMessage());
        }
        return authList;
	}

	@Override
	public boolean deleteAuth(int authId) throws DatabaseConnectionException {
		String sql = "DELETE FROM Auth WHERE authID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, authId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error deleting Auth :" + authId + ": " + e.getMessage());
        }
        return false;
	}

	@Override
	public Auth findByUsernameAndPassword(String username, String password) throws DatabaseConnectionException {
		String sql = "SELECT * FROM Auth WHERE username = ? AND password = ?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, username);
			pst.setString(2, password);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				try{
	                return mapToAuth(rs);
	            } catch(SQLException e){
	                e.printStackTrace();
	                throw new DatabaseConnectionException("Error mapping auth data");
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseConnectionException("Error while login");
		}
		return null;
	}

	@Override
	public boolean updatePassword(int customerId, String newPassword) {
		String sql = "UPDATE Auth SET password = ? WHERE customerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, customerId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error updating Auth for customer ID " + customerId+ ": " + e.getMessage());
        }
        return false;
	}

	@Override
	public boolean deleteAuthByCustomerId(int CustomerId) throws DatabaseConnectionException {
		String sql = "DELETE FROM Auth WHERE customerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, CustomerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error deleting Auth :customer Id: " + CustomerId + ": " + e.getMessage());
        }
        return false;
	}

}
