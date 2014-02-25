package org.deel.test;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.deel.domain.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation;


public class FilenameValidatorTest {

	
	private Validator validator;
	
	@Before
	public void setup() {
		ValidatorFactory vfactory = javax.validation.Validation.buildDefaultValidatorFactory();
		validator = vfactory.getValidator();
	}
	
	@Test
	public void testName() {
		
		File f = new File();
		f.setName("sad");
		
		Set<ConstraintViolation<File>> violations = validator.validate(f);
		
		
		Assert.assertTrue(violations.isEmpty());
	
	}
	
	@Test
	public void testIllegalName() {
	
		File f = new File();
		Set<ConstraintViolation<File>> violations;
	
		f.setName(".");
		violations = validator.validate(f);
		Assert.assertFalse(violations.isEmpty());
		
		f.setName("..");
		violations = validator.validate(f);
		Assert.assertFalse(violations.isEmpty());
		
		f.setName("../sadsad");
		violations = validator.validate(f);
		Assert.assertFalse(violations.isEmpty());
		
		f.setName(".dasda..das");
		violations = validator.validate(f);
		Assert.assertFalse(violations.isEmpty());
		
		f.setName("dsadsada../");
		violations = validator.validate(f);
		Assert.assertFalse(violations.isEmpty());
		
		f.setName("test..");
		violations = validator.validate(f);
		Assert.assertFalse(violations.isEmpty());
		
	}
}
