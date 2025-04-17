package com.careerhub.dao;

import java.util.List;

import com.careerhub.entity.Company;

public interface CompanyDao {
	// CRUD operations
    boolean addCompany(Company company);
    boolean updateCompany(Company company);
    boolean deleteCompany(int userID);
    Company getCompanyByID(int userID);
    List<Company> getAllCompanies();
	Company getCompanyByEmail(String email);
}
