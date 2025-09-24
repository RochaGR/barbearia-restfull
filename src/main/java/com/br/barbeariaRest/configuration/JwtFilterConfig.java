package com.br.barbeariaRest.configuration;

import com.br.barbeariaRest.util.JwtAuthFilter;
import com.br.barbeariaRest.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtFilterConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        return new JwtAuthFilter(userDetailsService, jwtUtil);
    }
}