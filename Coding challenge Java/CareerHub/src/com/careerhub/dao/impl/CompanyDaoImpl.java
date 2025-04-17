package com.careerhub.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.careerhub.dao.CompanyDao;
import com.careerhub.entity.Company;
import com.careerhub.util.DBConnection;

public class CompanyDaoImpl implements CompanyDao{
	
	private final Connection connection;

    public CompanyDaoImpl(String filename) {
    	this.connection = DBConnection.getConnection(filename);
    }
    
    private Company extractCompany(ResultSet rs) throws SQLException {
    	return new Company(
                rs.getInt("userID"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("companyName"),
                rs.getString("location")
            );
    }	

	@Override
	public boolean addCompany(Company company) {
		String userSql = "INSERT INTO Users (email, phone, firstName, lastName, userType) VALUES (?, ?, ?, ?, ?)";
        String companySql = "INSERT INTO Companies (companyID, companyName, location) VALUES (?, ?, ?)";

        try (PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
            userStmt.setString(1, company.getEmail());
            userStmt.setString(2, company.getPhone());
            userStmt.setString(3, company.getFirstName());
            userStmt.setString(4, company.getLastName());
            userStmt.setString(5, "COMPANY");

            int rowsInserted = userStmt.executeUpdate();
            if (rowsInserted == 0) return false;

            ResultSet rs = userStmt.getGeneratedKeys();
            if (rs.next()) {
                int userID = rs.getInt(1);
                company.setUserID(userID);

                try (PreparedStatement companyStmt = connection.prepareStatement(companySql)) {
                    companyStmt.setInt(1, userID);
                    companyStmt.setString(2, company.getCompanyName());
                    companyStmt.setString(3, company.getLocation());

                    return companyStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean updateCompany(Company company) {
		String userSql = "UPDATE Users SET email = ?, phone = ?, firstName = ?, lastName = ? WHERE userID = ?";
        String companySql = "UPDATE Companies SET companyName = ?, location = ? WHERE companyID = ?";

        try (
            PreparedStatement userStmt = connection.prepareStatement(userSql);
            PreparedStatement companyStmt = connection.prepareStatement(companySql)
        ) {
            userStmt.setString(1, company.getEmail());
            userStmt.setString(2, company.getPhone());
            userStmt.setString(3, company.getFirstName());
            userStmt.setString(4, company.getLastName());
            userStmt.setInt(5, company.getUserID());

            companyStmt.setString(1, company.getCompanyName());
            companyStmt.setString(2, company.getLocation());
            companyStmt.setInt(3, company.getUserID());

            return userStmt.executeUpdate() > 0 && companyStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean deleteCompany(int userID) {
		String companySql = "DELETE FROM Companies WHERE companyID = ?";
        String userSql = "DELETE FROM Users WHERE userID = ?";

        try (
            PreparedStatement companyStmt = connection.prepareStatement(companySql);
            PreparedStatement userStmt = connection.prepareStatement(userSql)
        ) {
            companyStmt.setInt(1, userID);
            companyStmt.executeUpdate();

            userStmt.setInt(1, userID);
            return userStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public Company getCompanyByID(int companyID) {
		String sql = "SELECT u.userID, u.email, u.phone, u.firstName, u.lastName, " +
                "c.companyName, c.location " +
                "FROM Users u JOIN Companies c ON u.userID = c.companyID WHERE c.companyID = ?";

   try (PreparedStatement stmt = connection.prepareStatement(sql)) {
       stmt.setInt(1, companyID);
       ResultSet rs = stmt.executeQuery();
       if (rs.next()) {
           return extractCompany(rs);
       }
   } catch (SQLException e) {
       e.printStackTrace();
   }

   return null;
	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> companies = new ArrayList<>();
        String sql = "SELECT u.userID, u.email, u.phone, u.firstName, u.lastName, c.companyName, c.location " +
                     "FROM Users u JOIN Companies c ON u.userID = c.companyID";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                companies.add(extractCompany(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
	}

	@Override
	public Company getCompanyByEmail(String email) {
		String sql = "SELECT * FROM Companies JOIN Users ON companyID=userId WHERE email = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return extractCompany(rs);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
