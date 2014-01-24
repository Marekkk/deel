package org.deel.controllers;

import java.util.HashMap;
import java.util.Map;

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
	public String companyList(ModelMap map) {
		return "company";
	}
	
	@RequestMapping("/admin/company/new")
	public @ResponseBody Map<String, Object> newCompany(@RequestBody Company company, BindingResult result) 
	{
		Map<String, Object> ret = new HashMap<String, Object>();
		
		if(result.hasErrors())
			ret.put("status", "failed");
		
		companyService.insertNewCompany(company);
		
		ret.put("status", "success");
		
		return ret;
	}
}
