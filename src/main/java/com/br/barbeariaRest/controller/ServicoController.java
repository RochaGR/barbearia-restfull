package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.model.Servico;
import com.br.barbeariaRest.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Servico> criar(@RequestBody Servico servico) {
        Servico salvo = service.salvar(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable int id, @RequestBody Servico servico) {
        Servico servicoBusca = service.buscarPorId(id);
        if (servicoBusca == null) {
            return ResponseEntity.notFound().build();
        }

        if (servico == null) {
            return ResponseEntity.badRequest().build();
        }
        servicoBusca.setNome(servico.getNome());
        servicoBusca.setDescricao(servico.getDescricao());
        servicoBusca.setPreco(servico.getPreco());
        servicoBusca.setDuracaoMinutos(servico.getDuracaoMinutos());
        servicoBusca.setAtivo(servico.isAtivo());

        return ResponseEntity.ok(service.salvar(servicoBusca));
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        List<Servico> lista = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable int id) {
        Servico servico = service.buscarPorId(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(servico);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable int id) {
        Servico servico = service.buscarPorId(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}