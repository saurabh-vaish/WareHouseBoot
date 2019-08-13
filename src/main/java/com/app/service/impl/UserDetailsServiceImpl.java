package com.app.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.User;
import com.app.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository repo;

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repo.findByUserEmail(username);
		
		System.out.println(user);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("No User Found");
		}
		else
		{
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			for (String role : user.getRoles()){
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			}

			return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPwd(), grantedAuthorities);
		}
	}
}