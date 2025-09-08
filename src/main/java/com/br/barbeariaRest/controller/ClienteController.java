package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.ClienteRequestDTO;
import com.br.barbeariaRest.dto.response.ClienteResponseDTO;
import com.br.barbeariaRest.security.CustomUserDetails;
import com.br.barbeariaRest.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;


    @PutMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> atualizarMeuPerfil(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ClienteRequestDTO clienteDTO) {
        Long usuarioId = userDetails.getUsuario().getId();
        ClienteResponseDTO atualizado = service.atualizarByUsuarioId(usuarioId, clienteDTO);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> meuPerfil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usuarioId = userDetails.getUsuario().getId();
        ClienteResponseDTO cliente = service.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = service.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> lista = service.findAll();
        return ResponseEntity.ok(lista);
    }
}