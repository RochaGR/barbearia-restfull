package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.AgendamentoRequestDTO;
import com.br.barbeariaRest.dto.response.AgendamentoResponseDTO;
import com.br.barbeariaRest.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody AgendamentoRequestDTO dto) {
        try {
            AgendamentoResponseDTO agendamento = service.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar agendamento: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        AgendamentoResponseDTO agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoRequestDTO dto) {
        try {
            AgendamentoResponseDTO agendamento = service.atualizar(id, dto);
            return ResponseEntity.ok(agendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar agendamento: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar agendamento: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String novoStatus = request.get("status");
            if (novoStatus == null) {
                return ResponseEntity.badRequest().body("Campo 'status' é obrigatório");
            }

            AgendamentoResponseDTO agendamento = service.alterarStatus(id, novoStatus);
            return ResponseEntity.ok(agendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao alterar status: " + e.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarPorCliente(clienteId));
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorBarbeiro(@PathVariable Long barbeiroId) {
        return ResponseEntity.ok(service.buscarPorBarbeiro(barbeiroId));
    }
}