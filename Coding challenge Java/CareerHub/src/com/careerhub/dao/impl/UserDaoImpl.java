package com.careerhub.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.careerhub.dao.UserDao;
import com.careerhub.entity.Applicant;
import com.careerhub.entity.Company;
import com.careerhub.entity.User;
import com.careerhub.exception.InvalidEmailFormatException;
import com.careerhub.exception.InvalidPhoneNumberFormatException;
import com.careerhub.util.DBConnection;

public class UserDaoImpl implements UserDao{

	
	private final Connection connection;

    public UserDaoImpl(String filename) {
    	this.connection = DBConnection.getConnection(filename);
    }
	
    private User extractUser(ResultSet rs) throws SQLException {
    	
    	String userType = rs.getString("userType");
        User user;

        if ("APPLICANT".equalsIgnoreCase(userType)) {
            user = new Applicant();
        } else if ("COMPANY".equalsIgnoreCase(userType)) {
            user = new Company();
        } else {
            throw new SQLException("Unknown userType: " + userType);
        }

        try {
        user.setUserID(rs.getInt("userID"));
        user.setPhone(rs.getString("phone"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setUserType(userType);
        user.setEmail(rs.getString("email"));
		} catch (InvalidEmailFormatException | SQLException | InvalidPhoneNumberFormatException e) {
			e.printStackTrace();
			return null;
		}
        
        return user;

	}
	
	@Override
	public boolean addUser(User user) {
		String sql = "INSERT INTO Users (email, phone, firstName, lastName, userType) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getUserType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public boolean updateUser(User user) {
		String sql = "UPDATE Users SET email = ?, phone = ?, firstName = ?, lastName = ?, userType = ? WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getUserType());
            stmt.setInt(6, user.getUserID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public boolean deleteUser(int userID) {
		String sql = "DELETE FROM Users WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public User getUserByID(int userID) {
		String sql = "SELECT * FROM Users WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}



	@Override
	public User getUserByEmail(String email) {
		String sql = "SELECT * FROM Users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
	}
	
}
