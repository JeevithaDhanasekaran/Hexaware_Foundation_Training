package com.careerhub.service.Impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.careerhub.dao.CompanyDao;
import com.careerhub.dao.impl.CompanyDaoImpl;
import com.careerhub.entity.Company;
import com.careerhub.service.CompanyService;

public class CompanyServiceImpl implements CompanyService{
	private CompanyDao companyDao;

    public CompanyServiceImpl(String fileName) {
        this.companyDao = new CompanyDaoImpl(fileName);
    }

    public boolean registerCompany(Company company) {
    	try {
            return companyDao.addCompany(company);
        } catch (Exception e) {
            System.err.println("Error registering company: " + e.getMessage());
            return false;
        }
    }

    public Set<Company> getAllCompanies() {
        return new LinkedHashSet<>(companyDao.getAllCompanies());
    }

    public Company getCompanyByID(int companyID) {
        return companyDao.getCompanyByID(companyID);
    }

	@Override
	public Company getCompanyByEmail(String email) {
		try {
	        return companyDao.getCompanyByEmail(email);
	    } catch (Exception e) {
	        System.err.println("Error fetching company: " + e.getMessage());
	        return null;
	    }
	}
}
