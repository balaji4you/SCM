package com.sec.AuthServerSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration

public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	TokenStore tokenStore;

	@Autowired	
	AuthenticationManager authenticationManger;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("client").secret(encoder.encode("secret"))
				.authorizedGrantTypes("password", "refresh_code").scopes("all").accessTokenValiditySeconds(2000).and()			
				.withClient("SCMXPert").secret(encoder.encode("secret"))
				.authorizedGrantTypes("password", "refresh_code").scopes("all")
				.accessTokenValiditySeconds(2000)
				;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).authenticationManager(authenticationManger);

	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	TokenStore tokenStore() {
		return new InMemoryTokenStore();

	}

}
