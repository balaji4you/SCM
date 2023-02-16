package com.sec.AuthServerSecurity.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;

import com.sec.AuthServerSecurity.entity.Role;
import com.sec.AuthServerSecurity.entity.User;
import com.sec.AuthServerSecurity.repo.RoleRepository;
import com.sec.AuthServerSecurity.repo.UserRepository;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

//@Service
@Service("userDetailsService")

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("in username "+username);
		try {
		// User user = userRepository.findByEmail(username);
		User user = userRepository.findByUserid(username);
		System.out.println("after user name "+user.getEmail());
		if (user.isEnabled() == false) {
			System.out.println("Please Contact Administrator........");
			throw new UserDeniedAuthorizationException("Please Contact Administrator.......");
		}
		if (user != null) {
			System.out.println("user is not null");
			List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
			return buildUserForAuthentication(user, authorities);
		} else {
			System.out.println("user is null");
			throw new UsernameNotFoundException("username not found");
		}
		}catch (Exception e) {
			// TODO: handle exception"
			System.out.println("exception "+e);
			return null;
		}
	}

	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<>();
		userRoles.forEach((role) -> {

			roles.add(new SimpleGrantedAuthority(role.getRole()));
		});

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		return grantedAuthorities;
	}

	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

}
