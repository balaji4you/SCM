package com.sec.AuthServerSecurity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Value("${react_ip}")
	private String ip;

	@Value("${react_port}")
	private String port;

	@Bean
	public FilterRegistrationBean customCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		System.out.println(ip);
		config.addAllowedOrigin("http://" + ip + ":" + port);         
                config.addAllowedOrigin("http://localhost:3000");
                config.addAllowedOrigin("http://127.0.0.1:8082");             
                config.addAllowedOrigin("http://localhost:8082");
                config.addAllowedOrigin("http://127.0.0.1:3000");
                config.addAllowedOrigin("http://54.90.208.131:3000");
                config.addAllowedOrigin("http://localhost:8081");
                config.addAllowedOrigin("http://127.0.0.1:8081");    
		System.out.println("http://" + ip + ":" + port);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
