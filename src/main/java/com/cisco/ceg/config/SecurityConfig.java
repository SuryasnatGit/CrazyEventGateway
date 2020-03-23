package com.cisco.ceg.config;

import static com.cisco.ceg.common.CEGConstants.ADMIN_ROLE;
import static com.cisco.ceg.common.CEGConstants.ALLOWED_END_POINT;
import static com.cisco.ceg.common.CEGConstants.ALL_ROLES;
import static com.cisco.ceg.common.CEGConstants.USER_ROLE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * Bean definitions for spring security
 * 
 * @author surya
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${ceg.auth.user}")
	private String authUser;

	@Value("${ceg.auth.password}")
	private String authPassword;

	@Value("${ceg.auth.admin.user}")
	private String authAdminUser;

	@Value("${ceg.auth.admin.password}")
	private String authAdminPassword;

	// Authentication : User --> Roles
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
				.withUser(authUser).password(authPassword).roles(USER_ROLE).and().withUser(authAdminUser).password(authAdminPassword)
				.roles(ALL_ROLES);
	}

	// Authorization : Role -> Access
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests().antMatchers(ALLOWED_END_POINT).hasRole(USER_ROLE).antMatchers(ALLOWED_END_POINT)
				.hasRole(ADMIN_ROLE)
				.and()
				.csrf().disable().headers().frameOptions().disable();
	}

	// TODO: to look into this later
	/*
	 * private PasswordEncoder createPasswordEncoder() { Map<String,
	 * PasswordEncoder> encoders = new HashMap<>(); encoders.put(BCRYPT_ENCODINGID,
	 * new BCryptPasswordEncoder()); encoders.put(PBKDF2_PWD_ENCODER, new
	 * Pbkdf2PasswordEncoder()); encoders.put(SCRYPT_PWD_ENCODER, new
	 * SCryptPasswordEncoder()); DelegatingPasswordEncoder encoder = new
	 * DelegatingPasswordEncoder(BCRYPT_ENCODINGID, encoders);
	 * encoder.setDefaultPasswordEncoderForMatches(new StandardPasswordEncoder());
	 * return encoder; }
	 */
}
