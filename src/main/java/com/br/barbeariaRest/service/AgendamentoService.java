package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.request.AgendamentoRequestDTO;
import com.br.barbeariaRest.dto.response.AgendamentoResponseDTO;
import com.br.barbeariaRest.dto.mapper.AgendamentoMapper;
import com.br.barbeariaRest.model.Agendamento;
import com.br.barbeariaRest.model.Cliente;
import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.model.Servico;
import com.br.barbeariaRest.repository.AgendamentoRepository;
import com.br.barbeariaRest.repository.ClienteRepository;
import com.br.barbeariaRest.repository.BarbeiroRepository;
import com.br.barbeariaRest.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public AgendamentoResponseDTO criar(AgendamentoRequestDTO dto) {
        // Validar se cliente existe
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Validar se barbeiro existe
        Barbeiro barbeiro = barbeiroRepository.findById(dto.getBarbeiroId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Validar se serviço existe
        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Validar se barbeiro está ativo
        if (!barbeiro.isAtivo()) {
            throw new RuntimeException("Barbeiro não está ativo");
        }

        // Validar se serviço está ativo
        if (!servico.isAtivo()) {
            throw new RuntimeException("Serviço não está ativo");
        }

        // Criar agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setDataHora(dto.getDataHora());
        agendamento.setStatus("AGENDADO");
        agendamento.setObservacoes(dto.getObservacoes());

        Agendamento agendamentoSalvo = repository.save(agendamento);
        return AgendamentoMapper.toDto(agendamentoSalvo);
    }

    public AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);
        if (agendamentoExistente.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        Agendamento agendamento = agendamentoExistente.get();

        // Validar e atualizar cliente se necessário
        if (!agendamento.getCliente().getId().equals(dto.getClienteId())) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            agendamento.setCliente(cliente);
        }

        // Validar e atualizar barbeiro se necessário
        if (!agendamento.getBarbeiro().getId().equals(dto.getBarbeiroId())) {
            Barbeiro barbeiro = barbeiroRepository.findById(dto.getBarbeiroId())
                    .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
            if (!barbeiro.isAtivo()) {
                throw new RuntimeException("Barbeiro não está ativo");
            }
            agendamento.setBarbeiro(barbeiro);
        }

        // Validar e atualizar serviço se necessário
        if (!agendamento.getServico().getId().equals(dto.getServicoId())) {
            Servico servico = servicoRepository.findById(dto.getServicoId())
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
            if (!servico.isAtivo()) {
                throw new RuntimeException("Serviço não está ativo");
            }
            agendamento.setServico(servico);
        }

        agendamento.setDataHora(dto.getDataHora());
        agendamento.setObservacoes(dto.getObservacoes());

        Agendamento agendamentoSalvo = repository.save(agendamento);
        return AgendamentoMapper.toDto(agendamentoSalvo);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado");
        }
        repository.deleteById(id);
    }

    public AgendamentoResponseDTO buscarPorId(Long id) {
        Optional<Agendamento> agendamento = repository.findById(id);
        return agendamento.map(AgendamentoMapper::toDto).orElse(null);
    }

    public List<AgendamentoResponseDTO> buscarTodos() {
        List<Agendamento> agendamentos = repository.findAll();
        return agendamentos.stream().map(AgendamentoMapper::toDto).toList();
    }

    public AgendamentoResponseDTO alterarStatus(Long id, String novoStatus) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);
        if (agendamentoExistente.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        // Validar status válidos
        List<String> statusValidos = List.of("AGENDADO",  "CONCLUIDO", "CANCELADO");
        if (!statusValidos.contains(novoStatus)) {
            throw new RuntimeException("Status inválido. Use: " + String.join(", ", statusValidos));
        }

        Agendamento agendamento = agendamentoExistente.get();
        agendamento.setStatus(novoStatus);

        Agendamento agendamentoSalvo = repository.save(agendamento);
        return AgendamentoMapper.toDto(agendamentoSalvo);
    }

    public List<AgendamentoResponseDTO> buscarPorCliente(Long clienteId) {
        List<Agendamento> agendamentos = repository.findAll();
        return agendamentos.stream()
                .filter(a -> a.getCliente().getId().equals(clienteId))
                .map(AgendamentoMapper::toDto)
                .toList();
    }

    public List<AgendamentoResponseDTO> buscarPorBarbeiro(Long barbeiroId) {
        List<Agendamento> agendamentos = repository.findAll();
        return agendamentos.stream()
                .filter(a -> a.getBarbeiro().getId().equals(barbeiroId))
                .map(AgendamentoMapper::toDto)
                .toList();
    }
}