package com.careerhub.service;

import java.util.List;
import java.util.Set;

import com.careerhub.entity.Applicant;

public interface ApplicantService {
	boolean registerApplicant(Applicant applicant);
    Set<Applicant> getAllApplicants();
    Applicant getApplicantByID(int applicantID);
    Applicant getApplicantByEmail(String email);
}
