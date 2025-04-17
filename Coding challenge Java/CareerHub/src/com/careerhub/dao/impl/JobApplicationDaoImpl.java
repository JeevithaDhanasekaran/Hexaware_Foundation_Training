package com.careerhub.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.careerhub.dao.ApplicantDao;
import com.careerhub.dao.JobApplicationDao;
import com.careerhub.dao.JobListingDao;
import com.careerhub.entity.Applicant;
import com.careerhub.entity.JobApplication;
import com.careerhub.entity.JobListing;
import com.careerhub.exception.ApplicationDeadlineException;
import com.careerhub.util.DBConnection;

public class JobApplicationDaoImpl implements JobApplicationDao{
	
	private final Connection connection;
    private final JobListingDao jobListingDao;
    private final ApplicantDao applicantDao;

    public JobApplicationDaoImpl(String filename) {
    	this.connection = DBConnection.getConnection(filename);
        this.jobListingDao = new JobListingDaoImpl(filename);
        this.applicantDao = new ApplicantDaoImpl(filename);
    }
    
    private JobApplication extractJobApplication(ResultSet rs) throws SQLException {
        int applicationID = rs.getInt("applicationID");
        int jobID = rs.getInt("jobID");
        int applicantID = rs.getInt("applicantID");

        JobListing jobListing = jobListingDao.getJobListingByID(jobID);
        Applicant applicant = applicantDao.getApplicantByID(applicantID);

        Date applicationDate = rs.getTimestamp("applicationDate");
        String coverLetter = rs.getString("coverLetter");

        return new JobApplication(applicationID, jobListing, applicant, applicationDate, coverLetter);
    }
    
    
	

	@Override
	public boolean addJobApplication(JobApplication jobApplication) throws ApplicationDeadlineException {
		// First, check if application is on time
	    if (!isApplicationOnTime(jobApplication)) {
	        throw new ApplicationDeadlineException("Application deadline has passed for this job.");
	    }

	    String sql = "INSERT INTO JobApplications (jobID, applicantID, applicationDate, coverLetter) " +
	                 "VALUES (?, ?, ?, ?)";

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, jobApplication.getJobListing().getJobID());
	        stmt.setInt(2, jobApplication.getApplicant().getUserID());
	        stmt.setTimestamp(3, new java.sql.Timestamp(jobApplication.getApplicationDate().getTime()));
	        stmt.setString(4, jobApplication.getCoverLetter());

	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;

	}

	@Override
	public boolean updateJobApplication(JobApplication jobApplication) {
		String sql = "UPDATE JobApplications SET jobID = ?, applicantID = ?, applicationDate = ?, coverLetter = ? " +
                "WHERE applicationID = ?";

   try (PreparedStatement stmt = connection.prepareStatement(sql)) {
       stmt.setInt(1, jobApplication.getJobListing().getJobID());
       stmt.setInt(2, jobApplication.getApplicant().getUserID());
       stmt.setTimestamp(3, new Timestamp(jobApplication.getApplicationDate().getTime()));
       stmt.setString(4, jobApplication.getCoverLetter());
       stmt.setInt(5, jobApplication.getApplicationID());

       return stmt.executeUpdate() > 0;
   } catch (SQLException e) {
       e.printStackTrace();
   }

   return false;
	}

	@Override
	public boolean deleteJobApplication(int applicationID) {
		String sql = "DELETE FROM JobApplications WHERE applicationID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicationID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public JobApplication getJobApplicationByID(int applicationID) {
		String sql = "SELECT * FROM JobApplications WHERE applicationID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicationID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractJobApplication(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
	}

	@Override
	public boolean isApplicationOnTime(JobApplication jobApplication) {
		Date applicationDate = jobApplication.getApplicationDate();
	    Date deadline = jobApplication.getJobListing().getDeadline();
	    return !applicationDate.after(deadline);
	}

	@Override
	public List<JobApplication> getAllJobApplications() {
		List<JobApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM JobApplications";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractJobApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
	}

	@Override
	public List<JobApplication> getJobApplicationsByApplicant(int applicantID) {
		List<JobApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM JobApplications WHERE applicantID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicantID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractJobApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
	}

	@Override
	public List<JobApplication> getJobApplicationsByJobListing(int jobID) {
		List<JobApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM JobApplications WHERE jobID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractJobApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
	}
	
	

}
