package org.deel.test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.print.Printable;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.AbstractDocument.Content;

import org.deel.controllers.CompanyController;
import org.deel.controllers.UserController;
import org.deel.domain.Company;
import org.deel.domain.User;
import org.deel.service.CompanyService;
import org.deel.service.CustomUserDetailsService;
import org.deel.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

public class CompanyControllerTest {

	@Mock
	private CompanyService companyService;

	@InjectMocks
	private CompanyController companyController;

	// @Autowired
	// private CustomUserDetailsService customDetailsService;

	private MockMvc mockMvc;

	@Before
	public void setup() {

		// Process mock annotations

		companyController = new CompanyController();
		MockitoAnnotations.initMocks(this);
		// Setup Spring test in standalone mode
		this.mockMvc = MockMvcBuilders.standaloneSetup(companyController)
				.build();

	}

	@Test
	public void listCompany() throws Exception {
		List<Company> cl = new LinkedList<Company>();

		Company c = new Company();
		c.setId((long) 1);
		c.setName("mySpa");
		cl.add(c);

		when(companyService.listCompany()).thenReturn(cl);

		this.mockMvc.perform(get("/admin/companies"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("companies"))
				.andExpect(model().attributeExists("companies[0]"));
	}

	@Test
	public void insertCompany() throws Exception {

		this.mockMvc
				.perform(
						post("/admin/company/new")
						.content("{name: \"mySpa\"}")
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON.getType(),
												MediaType.APPLICATION_JSON.getSubtype(), 
												Charset.forName("utf8"))))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"));

		verify(companyService).insertNewCompany(any(Company.class));
	}

	@Test
	public void deleteCompanyTest() throws Exception {

		this.mockMvc.perform(post("/admin/companies/delete").param("id", "1"))
				.andExpect(status().isOk());

		verify(companyService).deleteCompany(any(Company.class));
	}

	@Test
	public void editCompanyTest() throws Exception {

		this.mockMvc.perform(
				post("/admin/company/edit").param("id", "1").param("name",
						"newName")).andExpect(status().isOk());

		verify(companyService).editCompany(any(Company.class));
	}
}
