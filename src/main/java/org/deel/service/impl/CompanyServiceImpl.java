package org.deel.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.deel.dao.CompanyDAO;
import org.deel.domain.Company;
import org.deel.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyServiceImpl implements CompanyService {
	private CompanyDAO companyDAO;
	
	public CompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	@Autowired
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Transactional
	@Override
	public void insertNewCompany(Company c) {
		Long id = companyDAO.insertCompany(c);
		c.setId(id);
	}
	
	@Transactional
	@Override
	public void deleteCompany(Company c) {
		companyDAO.deleteCompany(c);
	}
	
	@Transactional
	@Override
	public void editCompany(Company c){
		companyDAO.updateCompany(c);
	}
	
	@Transactional
	@Override
	public List<Company> listCompany() {
		return companyDAO.companyList();
	}
}