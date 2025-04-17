package com.careerhub.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.careerhub.dao.ApplicantDao;
import com.careerhub.entity.Applicant;
import com.careerhub.exception.InvalidEmailFormatException;
import com.careerhub.exception.InvalidPhoneNumberFormatException;
import com.careerhub.util.DBConnection;

public class ApplicantDaoImpl implements ApplicantDao{
	
	private Connection connection;

    public ApplicantDaoImpl(String filename) {
        this.connection = DBConnection.getConnection(filename);
    }

    private Applicant extractApplicant(ResultSet rs) throws SQLException, InvalidEmailFormatException, InvalidPhoneNumberFormatException {
        Applicant applicant = new Applicant();
        applicant.setUserID(rs.getInt("applicantID"));
        applicant.setFirstName(rs.getString("firstName"));
        applicant.setLastName(rs.getString("lastName"));
        applicant.setEmail(rs.getString("email"));
        applicant.setPhone(rs.getString("phone"));
        applicant.setResume(rs.getString("Resume"));
        return applicant;
    }
	
	

	@Override
	public boolean addApplicant(Applicant applicantInp) {
		Applicant applicant = (Applicant) applicantInp;
	    
	    String userSql = "INSERT INTO Users (email, phone, firstName, lastName, userType) VALUES (?, ?, ?, ?, 'APPLICANT')";
	    String applicantSql = "INSERT INTO Applicants (applicantID, Resume) VALUES (?, ?)";

	    try (
	        PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
	        PreparedStatement applicantStmt = connection.prepareStatement(applicantSql)
	    ) {
	        // Insert into Users
	        userStmt.setString(1, applicant.getEmail());
	        userStmt.setString(2, applicant.getPhone());
	        userStmt.setString(3, applicant.getFirstName());
	        userStmt.setString(4, applicant.getLastName());

	        int userRowsInserted = userStmt.executeUpdate();

	        // Get generated userID
	        ResultSet generatedKeys = userStmt.getGeneratedKeys();
	        int generatedId = -1;
	        if (generatedKeys.next()) {
	            generatedId = generatedKeys.getInt(1);
	        } else {
	            throw new SQLException("Failed to retrieve generated user ID.");
	        }

	        // Insert into Applicants using userID
	        applicantStmt.setInt(1, generatedId);
	        applicantStmt.setString(2, applicant.getResume());

	        int applicantRowsInserted = applicantStmt.executeUpdate();

	        return userRowsInserted > 0 && applicantRowsInserted > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean updateApplicant(Applicant applicantInp) {
		Applicant applicant = (Applicant) applicantInp;

	    String userSql = "UPDATE Users SET email = ?, phone = ?, firstName = ?, lastName = ? WHERE userID = ?";
	    String applicantSql = "UPDATE Applicants SET Resume = ? WHERE applicantID = ?";

	    try (
	        PreparedStatement userStmt = connection.prepareStatement(userSql);
	        PreparedStatement applicantStmt = connection.prepareStatement(applicantSql)
	    ) {
	        userStmt.setString(1, applicant.getEmail());
	        userStmt.setString(2, applicant.getPhone());
	        userStmt.setString(3, applicant.getFirstName());
	        userStmt.setString(4, applicant.getLastName());
	        userStmt.setInt(5, applicant.getUserID());

	        applicantStmt.setString(1, applicant.getResume());
	        applicantStmt.setInt(2, applicant.getUserID()); // applicantID = userID

	        int userRowsUpdated = userStmt.executeUpdate();
	        int applicantRowsUpdated = applicantStmt.executeUpdate();

	        return userRowsUpdated > 0 && applicantRowsUpdated > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean deleteApplicant(int applicantID) {
		String applicantSql = "DELETE FROM Applicants WHERE applicantID = ?";
        String userSql = "DELETE FROM Users WHERE userID = ?";
        try (PreparedStatement applicantStmt = connection.prepareStatement(applicantSql);
        		PreparedStatement userStmt = connection.prepareStatement(userSql)) {

            applicantStmt.setInt(1, applicantID);
            int applicantRowsDeleted = applicantStmt.executeUpdate();
            
            userStmt.setInt(1, applicantID);
            int userRowsDeleted = userStmt.executeUpdate();

            return applicantRowsDeleted > 0 && userRowsDeleted>0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }	}

	@Override
	public Applicant getApplicantByID(int applicantID) {
		String sql = "SELECT * FROM Applicants a JOIN Users u ON a.applicantID = u.userID WHERE a.applicantID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicantID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractApplicant(rs);
            }
        } catch (SQLException | InvalidEmailFormatException | InvalidPhoneNumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return null;
	}

	@Override
	public List<Applicant> getAllApplicants() {
		List<Applicant> applicants = new ArrayList<>();
        String sql = "SELECT * FROM Applicants a JOIN Users u ON a.applicantID = u.userID";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                applicants.add(extractApplicant(rs));
            }
        } catch (SQLException | InvalidEmailFormatException | InvalidPhoneNumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return applicants;
	}
	
	@Override
	public Applicant getApplicantByEmail(String email) {
		String sql = "SELECT * FROM Applicants a JOIN Users u ON a.applicantID = u.userID WHERE u.email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractApplicant(rs);
            }
        } catch (SQLException | InvalidEmailFormatException | InvalidPhoneNumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return null;
	}

}
