package com.gdnext.openbanking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
 OAuth2 Resource Server configuration.

 In a production environment the API would expect a valid JWT issued
 by an OAuth2 Authorization Server (Client Credentials flow).

 For the purpose of this test task endpoints are temporarily left open
 because no external OAuth2 server is configured.
*/

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}