package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.ServicoRequestDTO;
import com.br.barbeariaRest.dto.response.ServicoResponseDTO;
import com.br.barbeariaRest.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicoResponseDTO> criar(@RequestBody ServicoRequestDTO servicoDTO) {
        ServicoResponseDTO salvo = service.criar(servicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ServicoRequestDTO servicoDTO) {
        ServicoResponseDTO atualizado = service.atualizar(id, servicoDTO);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listar() {
        List<ServicoResponseDTO> lista = service.findAll();
        return ResponseEntity.ok(lista);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") //
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}