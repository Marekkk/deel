package org.deel.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.deel.dao.CompanyDAO;
import org.deel.domain.Company;
import org.deel.domain.User;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyDaoImpl implements CompanyDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Long insertCompany(Company c) {
		return (Long) sessionFactory.getCurrentSession().save(c);
	}

	@Override
	public void deleteCompany(Company c) {
		sessionFactory.getCurrentSession().delete(c);
	}

	@Override
	public void updateCompany(Company c) {
		sessionFactory.getCurrentSession().update(c);
	}
	
	@Override
	public List<Company> companyList() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Company.class);
		return crit.list();
	}
}
