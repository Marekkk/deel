package org.deel.dao;

import java.util.List;

import org.deel.domain.Company;

public interface CompanyDAO {
	public Long insertCompany(Company c);
	public void deleteCompany(Company c);
	public void updateCompany(Company c);
	List<Company> companyList();
}
