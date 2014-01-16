package org.deel.dao;

import org.deel.domain.Company;

public interface CompanyDao {
	public void insertCompany(Company c);
	public void deleteCompany(Company c);
	public void updateCompany(Company c);
}
