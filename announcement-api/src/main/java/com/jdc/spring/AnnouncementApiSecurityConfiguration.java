package com.jdc.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jdc.spring.model.constants.Role;
import com.jdc.spring.security.JwtTokenFilter;
import com.jdc.spring.utils.exceptions.ApiSecurityExceptionResolver;

@Configuration
@EnableMethodSecurity
public class AnnouncementApiSecurityConfiguration {

	@Autowired
	private JwtTokenFilter jwtTokenFilter;

	@Autowired
	private ApiSecurityExceptionResolver apiSecurityExceptionResolver;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());
		http.cors(cors -> {
		});


		http.authorizeHttpRequests(req -> {
	        req.anyRequest().permitAll();
		});

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.httpBasic(basic -> basic.disable());

		http.exceptionHandling(config -> {
			config.accessDeniedHandler(apiSecurityExceptionResolver);
			config.authenticationEntryPoint(apiSecurityExceptionResolver);
		});

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
