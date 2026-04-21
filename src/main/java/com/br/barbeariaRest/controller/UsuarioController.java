package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.response.UsuarioResponseDTO;
import com.br.barbeariaRest.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UsuarioResponseDTO dto) {
        service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return service.listar();
    }
}