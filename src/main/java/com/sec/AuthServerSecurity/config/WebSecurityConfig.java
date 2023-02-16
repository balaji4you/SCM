package com.sec.AuthServerSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.headers()
		.xssProtection()
		.and()
		.contentSecurityPolicy("script-src 'self'");

		http.httpBasic().and().csrf().disable().authorizeRequests()
		.antMatchers("/SCMXPert/**")
//				.hasAnyAuthority("ROLE_SUPERADMIN").antMatchers("/superadmin/changestatus/**")
//				.hasAnyAuthority("ROLE_SUPERADMIN").antMatchers("/superadmin/resetpass/**")
//				.hasAnyAuthority("ROLE_SUPERADMIN").antMatchers("/superadmin/getroles/**")
//				.hasAnyAuthority("ROLE_SUPERADMIN").antMatchers("/superadmin/changerole/**")
//				.hasAnyAuthority("ROLE_SUPERADMIN").antMatchers("/superadmin/adduser/**")
//				.hasAnyAuthority("ROLE_SUPERADMIN")
		.hasAnyAuthority("SUPERADMIN");

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(ImmutableList.of("*"));
		configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
