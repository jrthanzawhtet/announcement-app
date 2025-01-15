package com.jdc.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jdc.spring.model.repo.AccountRepo;



@Service
public class AppUserDetailsService implements UserDetailsService{

	@Autowired
	private AccountRepo accountRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		var account = accountRepo.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		
		var builder = User.withUsername(username)
				.password(account.getPassword())
				.authorities(account.getRole().name());
		return builder.build();
	}
	
	

}
