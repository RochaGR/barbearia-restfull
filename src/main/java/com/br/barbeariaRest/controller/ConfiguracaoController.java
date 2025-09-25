package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.model.ConfiguracaoBarbearia;
import com.br.barbeariaRest.repository.ConfiguracaoBarbeariaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/configuracao")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoBarbeariaRepository repository;

    @GetMapping("/barbearia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfiguracaoBarbearia> getConfiguracao() {
        ConfiguracaoBarbearia config = repository.findByAtivoTrue()
                .orElseGet(this::criarConfiguracaoPadrao);

        return ResponseEntity.ok(config);
    }

    @PostMapping("/barbearia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfiguracaoBarbearia> salvarConfiguracao(
            @Valid @RequestBody ConfiguracaoBarbearia config) {

        // Desativar configuração atual
        repository.findByAtivoTrue().ifPresent(atual -> {
            atual.setAtivo(false);
            repository.save(atual);
        });

        config.setAtivo(true);
        ConfiguracaoBarbearia salva = repository.save(config);
        return ResponseEntity.ok(salva);
    }

    @PutMapping("/barbearia/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfiguracaoBarbearia> atualizarConfiguracao(
            @PathVariable Long id,
            @Valid @RequestBody ConfiguracaoBarbearia config) {

        return repository.findById(id)
                .map(existente -> {
                    existente.setHoraAbertura(config.getHoraAbertura());
                    existente.setHoraFechamento(config.getHoraFechamento());
                    existente.setIntervaloAgendamentoMinutos(config.getIntervaloAgendamentoMinutos());
                    existente.setAntecedenciaMinimaHoras(config.getAntecedenciaMinimaHoras());
                    existente.setDiasFuncionamento(config.getDiasFuncionamento());

                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ConfiguracaoBarbearia criarConfiguracaoPadrao() {
        ConfiguracaoBarbearia config = new ConfiguracaoBarbearia();
        config.setHoraAbertura(java.time.LocalTime.of(8, 0));
        config.setHoraFechamento(java.time.LocalTime.of(18, 0));
        config.setIntervaloAgendamentoMinutos(30);
        config.setAntecedenciaMinimaHoras(2);
        config.setDiasFuncionamento(java.util.Set.of(1, 2, 3, 4, 5, 6)); // Seg-Sáb
        config.setAtivo(true);

        return repository.save(config);
    }
}