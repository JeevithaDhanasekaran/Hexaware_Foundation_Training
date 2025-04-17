package com.careerhub.dao;

import java.util.List;

import com.careerhub.entity.Applicant;

public interface ApplicantDao {
	// CRUD operations
    boolean addApplicant(Applicant applicant);
    boolean updateApplicant(Applicant applicant);
    boolean deleteApplicant(int applicantID);
    Applicant getApplicantByID(int applicantID);
    List<Applicant> getAllApplicants();
	Applicant getApplicantByEmail(String email);
}
