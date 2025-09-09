package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.BarbeiroRequestDTO;
import com.br.barbeariaRest.dto.response.BarbeiroResponseDTO;
import com.br.barbeariaRest.security.CustomUserDetails;
import com.br.barbeariaRest.service.BarbeiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
@RequiredArgsConstructor
public class BarbeiroController {

    private final BarbeiroService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BarbeiroResponseDTO> criar(@RequestBody BarbeiroRequestDTO barbeiroDTO) {
        BarbeiroResponseDTO salvo = service.criarBarbeiro(barbeiroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/meu-perfil")
    public ResponseEntity<BarbeiroResponseDTO> atualizarMeuPerfil(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BarbeiroRequestDTO barbeiroDTO) {
        Long usuarioId = userDetails.getUsuario().getId();
        BarbeiroResponseDTO atualizado = service.atualizarByUsuarioId(usuarioId, barbeiroDTO);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/ativos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BarbeiroResponseDTO>> listarAtivos() {
        List<BarbeiroResponseDTO> lista = service.findAllAtivos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/meu-perfil")
    public ResponseEntity<BarbeiroResponseDTO> meuPerfil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usuarioId = userDetails.getUsuario().getId();
        BarbeiroResponseDTO barbeiro = service.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(barbeiro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        BarbeiroResponseDTO barbeiro = service.findById(id);
        return ResponseEntity.ok(barbeiro);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        List<BarbeiroResponseDTO> lista = service.findAll();
        return ResponseEntity.ok(lista);
    }
}