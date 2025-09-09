package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.AgendamentoRequestDTO;
import com.br.barbeariaRest.dto.response.AgendamentoResponseDTO;
import com.br.barbeariaRest.security.CustomUserDetails;
import com.br.barbeariaRest.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AgendamentoRequestDTO agendamentoDTO) {

        Long usuarioId = userDetails.getUsuario().getId(); //  Obter ID do usuário autenticado
        AgendamentoResponseDTO salvo = service.criarAgendamento(usuarioId, agendamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/meus-agendamentos")
    public ResponseEntity<List<AgendamentoResponseDTO>> meusAgendamentos(@AuthenticationPrincipal Long usuarioId) {
        List<AgendamentoResponseDTO> agendamentos = service.findByClienteUsuarioId(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentos);
    }

    @GetMapping("/barbeiro/meus-agendamentos")
    public ResponseEntity<List<AgendamentoResponseDTO>> agendamentosBarbeiro(@AuthenticationPrincipal Long usuarioId) {
        List<AgendamentoResponseDTO> agendamentos = service.findByBarbeiroUsuarioId(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentos);    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        AgendamentoResponseDTO agendamento = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(agendamento);    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal Long barbeiroUsuarioId) {
        AgendamentoResponseDTO atualizado = service.atualizarStatus(id, status, barbeiroUsuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(atualizado);    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @AuthenticationPrincipal Long usuarioId) {
        service.cancelarAgendamento(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // Endpoints específicos para administração

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodos() {
        List<AgendamentoResponseDTO> agendamentos = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(agendamentos);    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<AgendamentoResponseDTO> agendamentos = service.findByClienteId(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorBarbeiro(@PathVariable Long barbeiroId) {
        List<AgendamentoResponseDTO> agendamentos = service.findByBarbeiroId(barbeiroId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorStatus(@PathVariable String status) {
        List<AgendamentoResponseDTO> agendamentos = service.findByStatus(status);
        return ResponseEntity.ok(agendamentos);
    }
}