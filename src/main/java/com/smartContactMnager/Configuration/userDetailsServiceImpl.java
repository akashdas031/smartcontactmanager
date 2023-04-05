package com.smartContactMnager.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartContactMnager.Dao.userRepository;
import com.smartContactMnager.Entities.user;

public class userDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private userRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Fteching user from database
		
		user userByEmail = userRepo.getUserByEmail(username);
		
		if(userByEmail == null) {
			throw new UsernameNotFoundException("could not found user name !!");
		}
		
		customUserDetails cud=new customUserDetails(userByEmail);
		return cud;
	}

}
