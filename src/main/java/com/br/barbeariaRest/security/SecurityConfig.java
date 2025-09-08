package com.br.barbeariaRest.config;

import com.br.barbeariaRest.security.JwtAuthenticationFilter;
import com.br.barbeariaRest.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servicos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servicos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/barbeiros/ativos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/barbeiros/{id}").permitAll()

                        // Endpoints para CLIENTES
                        .requestMatchers(HttpMethod.GET, "/clientes/meu-perfil").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/clientes/meu-perfil").hasRole("CLIENTE")

                        // Endpoints para BARBEIROS
                        .requestMatchers(HttpMethod.GET, "/barbeiros/meu-perfil").hasRole("BARBEIRO")
                        .requestMatchers(HttpMethod.PUT, "/barbeiros/meu-perfil").hasRole("BARBEIRO")

                        // Endpoints para ADMIN
                        .requestMatchers(HttpMethod.POST, "/barbeiros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/clientes/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/barbeiros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/servicos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicos/**").hasRole("ADMIN")

                        // Qualquer outro endpoint requer autenticação
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}