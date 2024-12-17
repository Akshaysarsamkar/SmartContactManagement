package com.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.getUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(request -> 
                        request
                               
                                .requestMatchers("/admin/**").hasRole("ADMIN") // Only Admins can access admin pages (no 'ROLE_' prefix)
                                .requestMatchers("/user/**").hasRole("USER") // Only Users can access user pages (no 'ROLE_' prefix)
                                .requestMatchers("/**").permitAll() // Permit all other routes
                )
                .formLogin(login -> login.loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index")
                        .permitAll()) // Allow all users to access the login page
               .logout(logout -> logout
                       .permitAll()); // Allow all users to access logout functionality

        return http.build();
    }
}
