package org.deel.validator;

import org.deel.domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		if (clazz.equals(User.class))
			return true;
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "surname.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty");
		
		/* Some fields can be null */
		if (errors.hasErrors())
			return;
		
		User u = (User) o;
		
		if (u.getUsername().length() < 10 && u.getUsername().length() > 5 ) 
			errors.reject("username", "username.length");
		
		
		if (u.getName().length() >= 255) 
			errors.reject("name", "name.tooLong");
		

		if (u.getSurname().length() >= 255) 
			errors.reject("surname", "surname.tooLong");
		
		
		if (u.getPassword().length() < 5)
			errors.reject("password", "password.tooShort");

	}

}
