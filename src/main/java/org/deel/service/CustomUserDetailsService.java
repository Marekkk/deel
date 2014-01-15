package org.deel.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * A custom {@link UserDetailsService} where user information is retrieved from
 * a JPA repository
 */

public class CustomUserDetailsService implements UserDetailsService {

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns a populated {@link UserDetails} object. The username is first
	 * retrieved from the database and then mapped to a {@link UserDetails}
	 * object.
	 */
	public UserDetails loadUserByUsername(String username) {

		org.deel.domain.User domainUser = userService
				.findUserByUsername(username);

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		List<GrantedAuthority> authList = new LinkedList<GrantedAuthority>();
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));

		User u1 = new User(domainUser.getUsername(), domainUser.getPassword(),
				enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authList);
		return u1;
	}

	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical
	 * role
	 * 
	 * @param role
	 *            the numerical role
	 * @return a collection of {@link GrantedAuthority

	 */
	// public Collection<? extends GrantedAuthority> getAuthorities(Integer
	// role) {
	// List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
	// return authList;
	// }
	//
	// /**
	// * Converts a numerical role to an equivalent list of roles
	// *
	// * @param role
	// * the numerical role
	// * @return list of roles as as a list of {@link String}
	// */
	// public List<String> getRoles(Integer role) {
	// List<String> roles = new ArrayList<String>();
	// if (role.intValue() == 1) {
	// roles.add("ROLE_USER");
	// roles.add("ROLE_ADMIN");
	// } else if (role.intValue() == 2) {
	// roles.add("ROLE_USER");
	// }
	// return roles;
	// }
	//
	// /**
	// * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	// *
	// * @param roles
	// * {@link String} of roles
	// * @return list of granted authorities
	// */
	// public static List<GrantedAuthority> getGrantedAuthorities(
	// List<String> roles) {
	// List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	// for (String role : roles) {
	// authorities.add(new SimpleGrantedAuthority(role));
	// }
	// return authorities;
	// }
}