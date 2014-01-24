package org.deel.service;

import java.util.List;

import javax.transaction.Transactional;

import org.deel.domain.Company;


public interface CompanyService {

	@Transactional
	public void insertNewCompany(Company c);
	
	@Transactional
	public void deleteCompany(Company c);
	
	@Transactional 
	public void editCompany(Company c);
	
	@Transactional
	public List<Company> listCompany();
}
