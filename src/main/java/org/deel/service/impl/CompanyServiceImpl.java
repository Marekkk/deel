package org.deel.service.impl;

import org.deel.dao.CompanyDao;
import org.deel.domain.Company;
import org.deel.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	private CompanyDao companyDAO;

	@Override
	public void addCompany(Company c) {
		companyDAO.insertCompany(c);
	}
}
