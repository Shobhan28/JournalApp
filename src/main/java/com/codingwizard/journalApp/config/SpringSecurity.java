package com.codingwizard.journalApp.config;

import com.codingwizard.journalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Define authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Public APIs (no login required)
                        .requestMatchers("/public/**").permitAll()

                        // allow user registration
                        .requestMatchers(HttpMethod.POST, "/user/**").permitAll()

                        // Only ADMIN role can access
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/journal/**").hasRole("USER")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                // Enable Basic Authentication
                .httpBasic(Customizer.withDefaults())
                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    // Password encoder used to hash passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Authentication provider that tells Spring Security
    // how to load users and verify passwords
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Set custom user service
        authProvider.setUserDetailsService(userDetailsService);

        // Set password encoder
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}