package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.service.BarbeiroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroService service;

    public BarbeiroController(BarbeiroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Barbeiro> criar(@RequestBody Barbeiro barbeiro) {
        Barbeiro salvo = service.salvar(barbeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barbeiro> atualizar(@PathVariable int id, @RequestBody Barbeiro barbeiro) {
        Barbeiro barbeiroBusca = service.buscarPorId(id);
        if (barbeiroBusca == null) {
            return ResponseEntity.notFound().build();
        }

        if (barbeiro == null) {
            return ResponseEntity.badRequest().build();
        }

        barbeiroBusca.setNome(barbeiro.getNome());
        barbeiroBusca.setAtivo(barbeiro.isAtivo());
        barbeiroBusca.setEspecialidades(barbeiro.getEspecialidades());

        return ResponseEntity.ok(service.salvar(barbeiroBusca));
    }

    @GetMapping
    public ResponseEntity<List<Barbeiro>> listar() {
        List<Barbeiro> lista = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barbeiro> buscarPorId(@PathVariable int id) {
        Barbeiro barbeiro = service.buscarPorId(id);
        if (barbeiro == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(barbeiro);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable int id) {
        Barbeiro barbeiro = service.buscarPorId(id);
        if (barbeiro == null) {
            return ResponseEntity.notFound().build();
        }
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}