package com.careerhub.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.careerhub.dao.CompanyDao;
import com.careerhub.dao.JobListingDao;
import com.careerhub.entity.Company;
import com.careerhub.entity.JobListing;
import com.careerhub.util.DBConnection;

public class JobListingDaoImpl implements JobListingDao{
	
	private final Connection connection;
    private final CompanyDao companyDao;

    public JobListingDaoImpl(String filename) {
    	this.connection = DBConnection.getConnection(filename);
        this.companyDao = new CompanyDaoImpl(filename);
    }
    
    private JobListing extractJobListing(ResultSet rs) throws SQLException {
    	try {
            int jobID = rs.getInt("jobID");
            int companyID = rs.getInt("companyID");
            Company company = companyDao.getCompanyByID(companyID);

            String title = rs.getString("jobTitle");
            String desc = rs.getString("jobDescription");
            String location = rs.getString("jobLocation");
            BigDecimal salary = rs.getBigDecimal("salary");
            String jobType = rs.getString("jobType");
            Date postedDate = rs.getTimestamp("postedDate");
            Date deadline = rs.getTimestamp("deadline");

            return new JobListing(jobID, company, title, desc, location, salary, jobType, postedDate, deadline);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error extracting job data", e);
        }
    }    

	@Override
	public boolean addJobListing(JobListing jobListing) {
		String sql = "INSERT INTO JobListings (companyID, jobTitle, jobDescription, jobLocation, salary, jobType, postedDate, deadline) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

   try (PreparedStatement stmt = connection.prepareStatement(sql)) {
       stmt.setInt(1, jobListing.getCompany().getUserID());
       stmt.setString(2, jobListing.getJobTitle());
       stmt.setString(3, jobListing.getJobDescription());
       stmt.setString(4, jobListing.getLocation());
       stmt.setBigDecimal(5, jobListing.getSalary());
       stmt.setString(6, jobListing.getJobType());
       stmt.setTimestamp(7, new Timestamp(jobListing.getPostedDate().getTime()));
       stmt.setTimestamp(8, new Timestamp(jobListing.getDeadline().getTime()));

       return stmt.executeUpdate() > 0;
   } catch (SQLException e) {
       e.printStackTrace();
   }

   return false;
	}

	@Override
	public boolean updateJobListing(JobListing jobListing) {
		String sql = "UPDATE JobListings SET companyID = ?, jobTitle = ?, jobDescription = ?, jobLocation = ?, salary = ?, " +
                "jobType = ?, postedDate = ?, deadline = ? WHERE jobID = ?";

   try (PreparedStatement stmt = connection.prepareStatement(sql)) {
       stmt.setInt(1, jobListing.getCompany().getUserID());
       stmt.setString(2, jobListing.getJobTitle());
       stmt.setString(3, jobListing.getJobDescription());
       stmt.setString(4, jobListing.getLocation());
       stmt.setBigDecimal(5, jobListing.getSalary());
       stmt.setString(6, jobListing.getJobType());
       stmt.setTimestamp(7, new Timestamp(jobListing.getPostedDate().getTime()));
       stmt.setTimestamp(8, new Timestamp(jobListing.getDeadline().getTime()));
       stmt.setInt(9, jobListing.getJobID());

       return stmt.executeUpdate() > 0;
   } catch (SQLException e) {
       e.printStackTrace();
   }

   return false;
	}

	@Override
	public boolean deleteJobListing(int jobID) {
		String sql = "DELETE FROM JobListings WHERE jobID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public JobListing getJobListingByID(int jobID) {
		String sql = "SELECT * FROM JobListings WHERE jobID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractJobListing(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
	}

	@Override
	public List<JobListing> getAllJobListings() {
		List<JobListing> list = new ArrayList<>();
        String sql = "SELECT * FROM JobListings";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractJobListing(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
	}

	@Override
	public List<JobListing> getJobListingsByCompany(int companyID) {
		List<JobListing> list = new ArrayList<>();
        String sql = "SELECT * FROM JobListings WHERE companyID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, companyID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractJobListing(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
	}
	
}
