package com.careerhub.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.careerhub.dao.ApplicantDao;
import com.careerhub.dao.impl.ApplicantDaoImpl;
import com.careerhub.entity.Applicant;
import com.careerhub.service.ApplicantService;

public class ApplicantServiceImpl implements ApplicantService{

	private ApplicantDao applicantDao;

    public ApplicantServiceImpl(String fileName) {
        this.applicantDao = new ApplicantDaoImpl(fileName);
    }

    public boolean registerApplicant(Applicant applicant) {
        try {
            return applicantDao.addApplicant(applicant);
        } catch (Exception e) {
            System.err.println("Error registering applicant: " + e.getMessage());
            return false;
        }
    }

    public Set<Applicant> getAllApplicants() {
        return new HashSet<>(applicantDao.getAllApplicants());
    }

    public Applicant getApplicantByID(int applicantID) {
        return applicantDao.getApplicantByID(applicantID);
    }
    
    @Override
    public Applicant getApplicantByEmail(String email) {
        try {
            return applicantDao.getApplicantByEmail(email);
        } catch (Exception e) {
            System.err.println("Error fetching applicant: " + e.getMessage());
            return null;
        }
    }

}
