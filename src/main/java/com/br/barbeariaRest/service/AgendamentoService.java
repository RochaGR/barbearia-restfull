package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.mapper.AgendamentoMapper;
import com.br.barbeariaRest.dto.request.AgendamentoRequestDTO;
import com.br.barbeariaRest.dto.response.AgendamentoResponseDTO;
import com.br.barbeariaRest.model.*;
import com.br.barbeariaRest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoMapper agendamentoMapper;

    @Transactional
    public AgendamentoResponseDTO criarAgendamento(Long usuarioId, AgendamentoRequestDTO dto) {
        Cliente cliente = clienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Barbeiro barbeiro = barbeiroRepository.findById(dto.getBarbeiroId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        //  Verificar conflito de agendamento
        verificarConflitoAgendamento(dto.getBarbeiroId(), dto.getDataHora());

        Agendamento agendamento = agendamentoMapper.toEntity(dto);
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setStatus("AGENDADO"); // Definir status inicial

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return agendamentoMapper.toResponseDTO(salvo);
    }

    public List<AgendamentoResponseDTO> findByClienteUsuarioId(Long usuarioId) {
        return agendamentoRepository.findByClienteUsuarioId(usuarioId)
                .stream()
                .map(agendamentoMapper::toResponseDTO)
                .toList();
    }

    public List<AgendamentoResponseDTO> findByBarbeiroUsuarioId(Long usuarioId) {
        return agendamentoRepository.findByBarbeiroUsuarioId(usuarioId)
                .stream()
                .map(agendamentoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public AgendamentoResponseDTO atualizarStatus(Long id, String status, Long barbeiroUsuarioId) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (!agendamento.getBarbeiro().getUsuario().getId().equals(barbeiroUsuarioId)) {
            throw new RuntimeException("Barbeiro não autorizado para alterar este agendamento");
        }

        // Validação de status
        if (!isStatusValido(status)) {
            throw new RuntimeException("Status inválido: " + status);
        }

        agendamento.setStatus(status);
        Agendamento atualizado = agendamentoRepository.save(agendamento);

        return agendamentoMapper.toResponseDTO(atualizado);
    }

    public AgendamentoResponseDTO findById(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        return agendamentoMapper.toResponseDTO(agendamento);
    }

    // Métodos auxiliares
    private void verificarConflitoAgendamento(Long barbeiroId, LocalDateTime dataHora) {
        LocalDateTime inicio = dataHora.minusMinutes(30);
        LocalDateTime fim = dataHora.plusMinutes(30);

        List<Agendamento> conflitos = agendamentoRepository.findByBarbeiroIdAndDataHoraBetween(
                barbeiroId, inicio, fim);

        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Já existe um agendamento para este barbeiro no horário selecionado");
        }
    }

    private boolean isStatusValido(String status) {
        return List.of("AGENDADO", "CONFIRMADO", "CONCLUIDO", "CANCELADO")
                .contains(status.toUpperCase());
    }

    // Metodo para cancelar agendamento pelo cliente

    @Transactional
    public void cancelarAgendamento(Long id, Long usuarioId) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (!agendamento.getCliente().getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não autorizado para cancelar este agendamento");
        }

        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);
    }

    public List<AgendamentoResponseDTO> findAll() {
        return agendamentoRepository.findAll()
                .stream()
                .map(agendamentoMapper::toResponseDTO)
                .toList();
    }

    public List<AgendamentoResponseDTO> findByClienteId(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId)
                .stream()
                .map(agendamentoMapper::toResponseDTO)
                .toList();
    }

    public List<AgendamentoResponseDTO> findByBarbeiroId(Long barbeiroId) {
        return agendamentoRepository.findByBarbeiroId(barbeiroId)
                .stream()
                .map(agendamentoMapper::toResponseDTO)
                .toList();
    }

    public List<AgendamentoResponseDTO> findByStatus(String status) {
        return agendamentoRepository.findByStatus(status)
                .stream()
                .map(agendamentoMapper::toResponseDTO)
                .toList();
    }
}