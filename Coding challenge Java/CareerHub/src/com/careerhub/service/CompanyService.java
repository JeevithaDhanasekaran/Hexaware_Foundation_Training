package com.careerhub.service;

import java.util.List;
import java.util.Set;

import com.careerhub.entity.Company;

public interface CompanyService {
	boolean registerCompany(Company company);
    Set<Company> getAllCompanies();
    Company getCompanyByID(int companyID);
    Company getCompanyByEmail(String email);
}
