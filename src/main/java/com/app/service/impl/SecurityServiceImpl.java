package com.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.app.service.SecurityService;


@Service
public class SecurityServiceImpl implements SecurityService {

	
	@Autowired
	UserDetailsService userDetails;
	
//	@Autowired
//	AuthenticationManager manager;
	
	
	@Override
	public boolean login(String username, String password) {
		
	/*
		UserDetails user = userDetails.loadUserByUsername(username);
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password,user.getAuthorities());
		
		manager.authenticate(token);
		
		boolean result = token.isAuthenticated();
		
		if(result)
		{
			SecurityContextHolder.getContext().setAuthentication(token);  // after login spring will store details and not ask for login again and again
		}
		
		return result;
	 */
		return true;
	}

}
