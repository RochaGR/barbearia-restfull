package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.Agendamento;
import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.model.Cliente;
import com.br.barbeariaRest.model.Servico;
import com.br.barbeariaRest.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteService clienteService;
    private final BarbeiroService barbeiroService;
    private final ServicoService servicoService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              ClienteService clienteService,
                              BarbeiroService barbeiroService,
                              ServicoService servicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteService = clienteService;
        this.barbeiroService = barbeiroService;
        this.servicoService = servicoService;
    }

    public Agendamento salvar(Agendamento agendamento) {
        // Validar cliente
        Cliente cliente = clienteService.buscarPorId(agendamento.getCliente().getId());
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }

        // Validar barbeiro
        Barbeiro barbeiro = barbeiroService.buscarPorId(agendamento.getBarbeiro().getId());
        if (barbeiro == null || !barbeiro.isAtivo()) {
            throw new RuntimeException("Barbeiro não encontrado ou inativo");
        }

        // Validar serviço
        Servico servico = servicoService.buscarPorId(agendamento.getServico().getId());
        if (servico == null || !servico.isAtivo()) {
            throw new RuntimeException("Serviço não encontrado ou inativo");
        }

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(int id) {
        return agendamentoRepository.findById(id).orElse(null);
    }

    public void excluir(int id) {
        agendamentoRepository.deleteById(id);
    }


    public List<Agendamento> buscarPorCliente(int clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public List<Agendamento> buscarPorBarbeiro(int barbeiroId) {
        return agendamentoRepository.findByBarbeiroId(barbeiroId);
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(23, 59, 59);
        return agendamentoRepository.findByDataHoraBetween(inicioDia, fimDia);
    }

    public List<Agendamento> buscarPorStatus(String status) {
        return agendamentoRepository.findByStatus(status);
    }
}