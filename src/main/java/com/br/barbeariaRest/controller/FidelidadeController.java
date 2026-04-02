package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.model.ConfiguracaoFidelidade;
import com.br.barbeariaRest.model.CupomDesconto;
import com.br.barbeariaRest.model.FidelidadeCliente;
import com.br.barbeariaRest.repository.ConfiguracaoFidelidadeRepository;
import com.br.barbeariaRest.service.FidelidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FidelidadeController {

    @Autowired
    private FidelidadeService fidelidadeService;

    @Autowired
    private ConfiguracaoFidelidadeRepository configRepo;

    @GetMapping("/fidelidade/cliente/{clienteId}")
    public ResponseEntity<?> getFidelidade(@PathVariable Long clienteId) {
        try {
            FidelidadeCliente fidelidade = fidelidadeService.buscarOuCriarFidelidade(clienteId);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("clienteId", fidelidade.getCliente().getId());
            response.put("cortesRealizados", fidelidade.getCortesRealizados());
            response.put("totalCuponsGerados", fidelidade.getTotalCuponsGerados());
            response.put("updatedAt", fidelidade.getUpdatedAt());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/fidelidade/cliente/{clienteId}/cupons")
    public ResponseEntity<?> getCuponsAtivos(@PathVariable Long clienteId) {
        List<CupomDesconto> cupons = fidelidadeService.listarCuponsAtivos(clienteId);
        List<Map<String, Object>> payload = cupons.stream().map(cupom -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("codigo", cupom.getCodigo());
            item.put("percentualDesconto", cupom.getPercentualDesconto());
            item.put("status", cupom.getStatus());
            item.put("dataExpiracao", cupom.getDataExpiracao());
            return item;
        }).toList();
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/fidelidade/cupom/validar")
    public ResponseEntity<?> validarCupom(@RequestBody Map<String, String> body) {
        try {
            CupomDesconto cupom = fidelidadeService.validarCupom(body.get("codigo"));
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("codigo", cupom.getCodigo());
            response.put("clienteId", cupom.getCliente().getId());
            response.put("percentualDesconto", cupom.getPercentualDesconto());
            response.put("status", cupom.getStatus());
            response.put("dataExpiracao", cupom.getDataExpiracao());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/fidelidade/config")
    public ResponseEntity<?> getConfig() {
        return ResponseEntity.ok(configRepo.findByAtivoTrue());
    }

    @PostMapping("/admin/fidelidade/config")
    public ResponseEntity<?> salvarConfig(@RequestBody ConfiguracaoFidelidade config) {
        configRepo.findByAtivoTrue().ifPresent(atual -> {
            atual.setAtivo(false);
            configRepo.save(atual);
        });
        config.setAtivo(true);
        return ResponseEntity.ok(configRepo.save(config));
    }

    @PutMapping("/admin/fidelidade/config/{id}")
    public ResponseEntity<?> atualizarConfig(@PathVariable Long id, @RequestBody ConfiguracaoFidelidade config) {
        return configRepo.findById(id).map(existente -> {
            existente.setCortesParaRecompensa(config.getCortesParaRecompensa());
            existente.setPercentualDesconto(config.getPercentualDesconto());
            existente.setValidadeCupomDias(config.getValidadeCupomDias());
            return ResponseEntity.ok(configRepo.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }
}

