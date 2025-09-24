package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.AuthRequestDTO;
import com.br.barbeariaRest.dto.request.ClienteRegistroDTO;
import com.br.barbeariaRest.dto.request.BarbeiroRegistroDTO;
import com.br.barbeariaRest.dto.response.AuthResponseDTO;
import com.br.barbeariaRest.dto.response.ClienteResponseDTO;
import com.br.barbeariaRest.dto.response.BarbeiroResponseDTO;
import com.br.barbeariaRest.service.ClienteService;
import com.br.barbeariaRest.service.BarbeiroService;
import com.br.barbeariaRest.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private BarbeiroService barbeiroService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping("/registro-cliente")
    public ResponseEntity<?> registrarCliente(@Valid @RequestBody ClienteRegistroDTO dto) {
        try {
            ClienteResponseDTO cliente = clienteService.registrarCliente(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao registrar cliente: ");
        }
    }

    @PostMapping("/registro-barbeiro")
    public ResponseEntity<?> registrarBarbeiro(@Valid @RequestBody BarbeiroRegistroDTO dto) {
        try {
            BarbeiroResponseDTO barbeiro = barbeiroService.registrarBarbeiro(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(barbeiro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao registrar barbeiro: " + e.getMessage());
        }
    }

}