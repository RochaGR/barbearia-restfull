package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.ServicoRequestDTO;
import com.br.barbeariaRest.dto.response.ServicoResponseDTO;
import com.br.barbeariaRest.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService service;

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ServicoRequestDTO dto) {
        try {
            ServicoResponseDTO servico = service.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(servico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar serviço: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ServicoResponseDTO servico = service.buscarPorId(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ServicoRequestDTO dto) {
        try {
            ServicoResponseDTO servico = service.atualizar(id, dto);
            return ResponseEntity.ok(servico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar serviço: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar serviço: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ServicoResponseDTO>> buscarAtivos() {
        return ResponseEntity.ok(service.buscarAtivos());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id) {
        try {
            ServicoResponseDTO servico = service.ativarDesativar(id);
            return ResponseEntity.ok(servico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao alterar status do serviço: " + e.getMessage());
        }
    }
}