package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.Agendamento;
import com.br.barbeariaRest.model.Cliente;
import com.br.barbeariaRest.model.ConfiguracaoFidelidade;
import com.br.barbeariaRest.model.CupomDesconto;
import com.br.barbeariaRest.model.FidelidadeCliente;
import com.br.barbeariaRest.repository.ClienteRepository;
import com.br.barbeariaRest.repository.ConfiguracaoFidelidadeRepository;
import com.br.barbeariaRest.repository.CupomDescontoRepository;
import com.br.barbeariaRest.repository.FidelidadeClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FidelidadeService {

    private final FidelidadeClienteRepository fidelidadeClienteRepository;
    private final CupomDescontoRepository cupomDescontoRepository;
    private final ConfiguracaoFidelidadeRepository configFidelidadeRepository;
    private final ClienteRepository clienteRepository;

    public FidelidadeCliente buscarFidelidadePorUsername(String username) {
        Cliente cliente = clienteRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return fidelidadeClienteRepository.findByClienteId(cliente.getId())
                .orElseGet(() -> {
                    FidelidadeCliente nova = new FidelidadeCliente();
                    nova.setCliente(cliente);
                    nova.setCortesRealizados(0);
                    nova.setTotalCuponsGerados(0);
                    return fidelidadeClienteRepository.save(nova);
                });
    }

    @Transactional
    public void registrarCorte(Long clienteId) {
        FidelidadeCliente fidelidade = buscarOuCriarFidelidade(clienteId);
        fidelidade.setCortesRealizados(fidelidade.getCortesRealizados() + 1);
        fidelidade.setUpdatedAt(LocalDateTime.now());

        ConfiguracaoFidelidade config = getConfigAtiva();

        if (fidelidade.getCortesRealizados() % config.getCortesParaRecompensa() == 0) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
            gerarCupom(cliente, config);
            fidelidade.setTotalCuponsGerados(fidelidade.getTotalCuponsGerados() + 1);
            log.info("Cupom gerado para cliente id={}", clienteId);
        }

        fidelidadeClienteRepository.save(fidelidade);
    }

    @Transactional
    public CupomDesconto gerarCupom(Cliente cliente, ConfiguracaoFidelidade config) {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        } while (cupomDescontoRepository.findByCodigo(codigo).isPresent());

        CupomDesconto cupom = new CupomDesconto();
        cupom.setCliente(cliente);
        cupom.setCodigo(codigo);
        cupom.setPercentualDesconto(config.getPercentualDesconto());
        cupom.setStatus(CupomDesconto.StatusCupom.ATIVO);
        cupom.setDataExpiracao(LocalDate.now().plusDays(config.getValidadeCupomDias()));

        return cupomDescontoRepository.save(cupom);
    }

    @Transactional
    public CupomDesconto validarCupom(String codigo) {
        CupomDesconto cupom = cupomDescontoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cupom nao encontrado"));

        if (cupom.getStatus() != CupomDesconto.StatusCupom.ATIVO) {
            throw new RuntimeException("Cupom nao esta ativo. Status: " + cupom.getStatus());
        }
        if (cupom.getDataExpiracao().isBefore(LocalDate.now())) {
            cupom.setStatus(CupomDesconto.StatusCupom.EXPIRADO);
            cupomDescontoRepository.save(cupom);
            throw new RuntimeException("Cupom expirado em " + cupom.getDataExpiracao());
        }
        return cupom;
    }

    @Transactional
    public void aplicarCupom(String codigo, Agendamento agendamento) {
        CupomDesconto cupom = validarCupom(codigo);
        
        BigDecimal precoOriginal = agendamento.getPrecoOriginal();
        BigDecimal percentual = cupom.getPercentualDesconto();
        
        BigDecimal desconto = precoOriginal.multiply(percentual)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal precoComDesconto = precoOriginal.subtract(desconto);
        
        agendamento.setPrecoFinal(precoComDesconto);
        
        cupom.setStatus(CupomDesconto.StatusCupom.USADO);
        cupom.setAgendamentoUso(agendamento);
        cupomDescontoRepository.save(cupom);
    }

    public FidelidadeCliente buscarOuCriarFidelidade(Long clienteId) {
        return fidelidadeClienteRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    Cliente cliente = clienteRepository.findById(clienteId)
                            .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
                    FidelidadeCliente nova = new FidelidadeCliente();
                    nova.setCliente(cliente);
                    nova.setCortesRealizados(0);
                    nova.setTotalCuponsGerados(0);
                    return fidelidadeClienteRepository.save(nova);
                });
    }

    public List<CupomDesconto> listarCuponsAtivos(Long clienteId) {
        return cupomDescontoRepository.findByClienteIdAndStatus(clienteId, CupomDesconto.StatusCupom.ATIVO);
    }

    public ConfiguracaoFidelidade getConfigAtiva() {
        return configFidelidadeRepository.findByAtivoTrue().orElseGet(() -> {
            ConfiguracaoFidelidade padrao = new ConfiguracaoFidelidade();
            padrao.setCortesParaRecompensa(5);
            padrao.setPercentualDesconto(new BigDecimal("40.00"));
            padrao.setValidadeCupomDias(30);
            padrao.setAtivo(true);
            return configFidelidadeRepository.save(padrao);
        });
    }

    @Transactional
    public void aplicarCupomSeExistente(Long clienteId, Agendamento agendamento) {
        log.info("Verificando cupom para cliente id={}", clienteId);
        
        List<CupomDesconto> cuponsAtivos = cupomDescontoRepository
                .findByClienteIdAndStatus(clienteId, CupomDesconto.StatusCupom.ATIVO);
        
        log.info("Cupons ativos encontrados: {}", cuponsAtivos.size());
        
        if (!cuponsAtivos.isEmpty()) {
            CupomDesconto cupom = cuponsAtivos.get(0);
            log.info("Cupom encontrado: codigo={}, percentual={}", cupom.getCodigo(), cupom.getPercentualDesconto());
            
            if (cupom.getDataExpiracao().isBefore(LocalDate.now())) {
                cupom.setStatus(CupomDesconto.StatusCupom.EXPIRADO);
                cupomDescontoRepository.save(cupom);
                log.info("Cupom expirado");
                return;
            }
            
            BigDecimal precoOriginal = agendamento.getPrecoOriginal();
            BigDecimal percentual = cupom.getPercentualDesconto();
            
            BigDecimal desconto = precoOriginal.multiply(percentual)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal precoComDesconto = precoOriginal.subtract(desconto);
            
            log.info("Aplicando desconto: original={}, desconto={}, final={}", precoOriginal, desconto, precoComDesconto);
            
            agendamento.setPrecoFinal(precoComDesconto);
            
            cupom.setStatus(CupomDesconto.StatusCupom.USADO);
            cupom.setAgendamentoUso(agendamento);
            cupomDescontoRepository.save(cupom);
            
            FidelidadeCliente fidelidade = fidelidadeClienteRepository.findByClienteId(clienteId)
                    .orElse(null);
            if (fidelidade != null) {
                fidelidade.setCortesRealizados(0);
                
                BigDecimal atual = fidelidade.getTotalEconomizado() != null 
                        ? fidelidade.getTotalEconomizado() 
                        : BigDecimal.ZERO;
                fidelidade.setTotalEconomizado(atual.add(desconto));
                
                fidelidadeClienteRepository.save(fidelidade);
            }
        }
    }
}

