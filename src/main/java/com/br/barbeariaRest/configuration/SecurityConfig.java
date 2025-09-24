package com.br.barbeariaRest.configuration;

import com.br.barbeariaRest.repository.UsuarioRepository;
import com.br.barbeariaRest.util.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()

                        // Endpoints administrativos
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Clientes
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "CLIENTE", "BARBEIRO")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("ADMIN")

                        // Barbeiros
                        .requestMatchers(HttpMethod.GET, "/barbeiros/**").hasAnyRole("ADMIN", "CLIENTE", "BARBEIRO")
                        .requestMatchers(HttpMethod.PUT, "/barbeiros/**").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.DELETE, "/barbeiros/**").hasRole("ADMIN")

                        // Serviços
                        .requestMatchers(HttpMethod.GET, "/servicos/**").hasAnyRole("ADMIN", "CLIENTE", "BARBEIRO")
                        .requestMatchers(HttpMethod.POST, "/servicos/**").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.PUT, "/servicos/**").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.PATCH, "/servicos/**").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.DELETE, "/servicos/**").hasRole("ADMIN")

                        // Agendamentos
                        .requestMatchers(HttpMethod.GET, "/agendamentos").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/cliente/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/barbeiro/**").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/agendamentos/**").hasAnyRole("ADMIN", "CLIENTE", "BARBEIRO")
                        .requestMatchers(HttpMethod.PATCH, "/agendamentos/**").hasAnyRole("ADMIN", "BARBEIRO")
                        .requestMatchers(HttpMethod.DELETE, "/agendamentos/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    List<GrantedAuthority> authorities = usuario.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNome()))
                            .collect(Collectors.toList());
                    return new org.springframework.security.core.userdetails.User(
                            usuario.getUsername(),
                            usuario.getPassword(),
                            authorities
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}