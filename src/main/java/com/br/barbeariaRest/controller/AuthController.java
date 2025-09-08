package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.LoginRequestDTO;
import com.br.barbeariaRest.dto.request.UsuarioRequestDTO;
import com.br.barbeariaRest.dto.response.LoginResponseDTO;
import com.br.barbeariaRest.dto.response.UsuarioResponseDTO;
import com.br.barbeariaRest.service.AuthService;
import com.br.barbeariaRest.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ClienteService clienteService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        try {
            LoginResponseDTO response = authService.login(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        try {
            // 1. Cria o usuário básico
            UsuarioResponseDTO usuarioCriado = authService.registrar(usuarioDTO);

            // 2. Cria automaticamente o perfil de cliente associado
            clienteService.criarClienteAutomatico(usuarioCriado.getId(), usuarioDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}