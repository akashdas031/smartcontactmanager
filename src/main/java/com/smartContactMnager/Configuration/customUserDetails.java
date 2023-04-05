package com.smartContactMnager.Configuration;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartContactMnager.Entities.user;

public class customUserDetails implements UserDetails {

	private user u;
	
	
	public customUserDetails(user u) {
		super();
		this.u = u;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(u.getRole());
		
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		
		return u.getPassword();
	}

	@Override
	public String getUsername() {
		
		return u.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

}
