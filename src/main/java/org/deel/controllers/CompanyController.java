package org.deel.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.deel.dao.CompanyDAO;
import org.deel.dao.impl.CompanyDaoImpl;
import org.deel.domain.Company;
import org.deel.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CompanyController {
	
	private CompanyService companyService;
	
	public CompanyService getCompanyService() {
		return companyService;
	}

	@Autowired
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	@RequestMapping("/admin/company/")

	public String companyPage(ModelMap map) {
		return "company";
	}

	@RequestMapping("/admin/company/list")
	public @ResponseBody Map<String, Object> companyList() 
	{
	

		Map<String, Object> ret = new HashMap<String, Object>();
		
		Set<Company> companies = new HashSet<Company>();
		for (Company c : companyService.listCompany())
			companies.add(c);
		
		ret.put("status", "success");
		ret.put("companies", companies);
		
		return ret;
	}

	@RequestMapping("/admin/company/edit")
	public @ResponseBody Map<String, Object> editCompany(@RequestBody Company company, BindingResult result) 
	{
		Map<String, Object> ret = new HashMap<String, Object>();
		
		if(result.hasErrors())
			ret.put("status", "failed");
		
		companyService.editCompany(company);
		
		ret.put("status", "success");
		
		return ret;
	}
	
	@RequestMapping("/admin/company/remove")
	public @ResponseBody Map<String, Object> removeCompany(@RequestBody Company company, BindingResult result) 
	{
		Map<String, Object> ret = new HashMap<String, Object>();
		
	
		companyService.deleteCompany(company);
		
		ret.put("status", "success");
		
		return ret;
	}
	
	@RequestMapping("/admin/company/new")
	public @ResponseBody Map<String, Object> newCompany(@RequestBody Company company, BindingResult result) 
	{
		Map<String, Object> ret = new HashMap<String, Object>();
		
		if(result.hasErrors())
			ret.put("status", "failed");
		
		companyService.insertNewCompany(company);
		
		ret.put("status", "success");
		ret.put("company", company);
		return ret;
	}
}
