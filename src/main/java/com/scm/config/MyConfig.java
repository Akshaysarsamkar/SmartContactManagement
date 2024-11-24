package com.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MyConfig{


    @Bean
    UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.getUserDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;	
	}


    //configure method
   
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    	
    	http.authorizeHttpRequests(auth -> auth.
    			requestMatchers("/admin/**").hasAnyRole("Admin")
    			.requestMatchers("/user/**").hasAnyRole("USER_ROLE")
    			.requestMatchers("/").permitAll()
    			.anyRequest().authenticated()
    		).formLogin(form-> form
    				.loginPage("/login")
    				.permitAll()).logout( Logout -> Logout.permitAll());
    	
    	return http.build();
    }
		 
	
	
	
}
