package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.model.Agendamento;
import com.br.barbeariaRest.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Agendamento> criar(@RequestBody Agendamento agendamento) {
        Agendamento salvo = service.salvar(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable int id, @RequestBody Agendamento agendamento) {
        Agendamento agendamentoBusca = service.buscarPorId(id);
        if (agendamentoBusca == null) {
            return ResponseEntity.notFound().build();
        }

        if (agendamento == null) {
            return ResponseEntity.badRequest().build();
        }

        agendamentoBusca.setCliente(agendamento.getCliente());
        agendamentoBusca.setBarbeiro(agendamento.getBarbeiro());
        agendamentoBusca.setServico(agendamento.getServico());
        agendamentoBusca.setDataHora(agendamento.getDataHora());
        agendamentoBusca.setStatus(agendamento.getStatus());
        agendamentoBusca.setObservacoes(agendamento.getObservacoes());

        return ResponseEntity.ok(service.salvar(agendamentoBusca));
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar() {
        List<Agendamento> lista = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable int id) {
        Agendamento agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(agendamento);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable int id) {
        Agendamento agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints específicos para agendamentos

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Agendamento>> listarPorCliente(@PathVariable int clienteId) {
        List<Agendamento> agendamentos = service.buscarPorCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<Agendamento>> listarPorBarbeiro(@PathVariable int barbeiroId) {
        List<Agendamento> agendamentos = service.buscarPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<List<Agendamento>> listarPorData(@PathVariable String data) {
        try {
            LocalDate dataConsulta = LocalDate.parse(data);
            List<Agendamento> agendamentos = service.buscarPorData(dataConsulta);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Agendamento>> listarPorStatus(@PathVariable String status) {
        List<Agendamento> agendamentos = service.buscarPorStatus(status);
        return ResponseEntity.ok(agendamentos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(@PathVariable int id, @RequestParam String status) {
        Agendamento agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }

        agendamento.setStatus(status);
        Agendamento atualizado = service.salvar(agendamento);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Agendamento> cancelar(@PathVariable int id) {
        Agendamento agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }

        agendamento.setStatus("CANCELADO");
        Agendamento atualizado = service.salvar(agendamento);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Agendamento> confirmar(@PathVariable int id) {
        Agendamento agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }

        agendamento.setStatus("CONFIRMADO");
        Agendamento atualizado = service.salvar(agendamento);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Agendamento> concluir(@PathVariable int id) {
        Agendamento agendamento = service.buscarPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }

        agendamento.setStatus("CONCLUIDO");
        Agendamento atualizado = service.salvar(agendamento);
        return ResponseEntity.ok(atualizado);
    }
}