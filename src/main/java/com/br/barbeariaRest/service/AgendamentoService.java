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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    public AgendamentoResponseDTO criar(AgendamentoRequestDTO dto) {
        log.info("Criando agendamento para cliente={}, barbeiro={}, servico={}",
                dto.getClienteId(), dto.getBarbeiroId(), dto.getServicoId());

        try {
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

            // NOVA VALIDAÇÃO: Verificar disponibilidade completa
            if (!disponibilidadeService.isHorarioDisponivel(dto.getBarbeiroId(), dto.getDataHora(),
                    servico.getDuracaoMinutos())) {
                throw new RuntimeException("Horário não disponível para agendamento");
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
            log.info("Agendamento criado com sucesso id={}", agendamentoSalvo.getId());
            return AgendamentoMapper.toDto(agendamentoSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar agendamento para cliente={}: {}", dto.getClienteId(), e.getMessage());
            throw e;
        }
    }

    public AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto) {
        log.info("Atualizando agendamento id={}", id);

        try {
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
            Servico servico = servicoRepository.findById(dto.getServicoId())
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
            if (!servico.isAtivo()) {
                throw new RuntimeException("Serviço não está ativo");
            }
            agendamento.setServico(servico);

            // NOVA VALIDAÇÃO: Verificar disponibilidade para o novo horário
            if (!disponibilidadeService.isHorarioDisponivel(dto.getBarbeiroId(), dto.getDataHora(),
                    servico.getDuracaoMinutos())) {
                throw new RuntimeException("Horário não disponível para reagendamento");
            }

            agendamento.setDataHora(dto.getDataHora());
            agendamento.setObservacoes(dto.getObservacoes());

            Agendamento agendamentoSalvo = repository.save(agendamento);
            log.info("Agendamento atualizado com sucesso id={}", agendamentoSalvo.getId());
            return AgendamentoMapper.toDto(agendamentoSalvo);
        } catch (Exception e) {
            log.error("Erro ao atualizar agendamento id={}: {}", id, e.getMessage());
            throw e;
        }
    }

    public void excluir(Long id) {
        log.info("Excluindo agendamento id={}", id);

        if (!repository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado");
        }
        repository.deleteById(id);

        log.info("Agendamento excluído com sucesso id={}", id);
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
        log.info("Alterando status do agendamento id={} para status={}", id, novoStatus);

        Optional<Agendamento> agendamentoExistente = repository.findById(id);
        if (agendamentoExistente.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        // Validar status válidos
        List<String> statusValidos = List.of("AGENDADO", "CONFIRMADO", "CONCLUIDO", "CANCELADO");
        if (!statusValidos.contains(novoStatus)) {
            throw new RuntimeException("Status inválido. Use: " + String.join(", ", statusValidos));
        }

        Agendamento agendamento = agendamentoExistente.get();
        agendamento.setStatus(novoStatus);

        Agendamento agendamentoSalvo = repository.save(agendamento);
        log.info("Status do agendamento alterado com sucesso id={}", agendamentoSalvo.getId());
        return AgendamentoMapper.toDto(agendamentoSalvo);
    }

    public List<AgendamentoResponseDTO> buscarPorCliente(Long clienteId) {
        List<Agendamento> agendamentos = repository.findByClienteId(clienteId);
        return agendamentos.stream()
                .map(AgendamentoMapper::toDto)
                .toList();
    }

    public List<AgendamentoResponseDTO> buscarPorBarbeiro(Long barbeiroId) {
        List<Agendamento> agendamentos = repository.findByBarbeiroId(barbeiroId);
        return agendamentos.stream()
                .map(AgendamentoMapper::toDto)
                .toList();
    }


    public List<AgendamentoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Agendamento> agendamentos = repository.findByDataHoraBetween(inicio, fim);
        return agendamentos.stream()
                .map(AgendamentoMapper::toDto)
                .toList();
    }


    public boolean hasConflito(Long barbeiroId, LocalDateTime inicio, LocalDateTime fim, Long agendamentoIdIgnorar) {
        return disponibilidadeService.hasConflitoAgendamento(barbeiroId, inicio, fim, agendamentoIdIgnorar);
    }
}