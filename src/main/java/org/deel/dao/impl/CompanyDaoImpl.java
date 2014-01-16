package org.deel.dao.impl;

import org.deel.dao.CompanyDao;
import org.deel.domain.Company;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyDaoImpl implements CompanyDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insertCompany(Company c) {
		sessionFactory.getCurrentSession().saveOrUpdate(c);
	}

	@Override
	public void deleteCompany(Company c) {
		sessionFactory.getCurrentSession().delete(c);
	}

	@Override
	public void updateCompany(Company c) {
		sessionFactory.getCurrentSession().update(c);
	}
}
