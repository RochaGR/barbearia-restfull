package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.request.AgendamentoRequestDTO;
import com.br.barbeariaRest.dto.response.AgendamentoResponseDTO;
import com.br.barbeariaRest.model.Agendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private  BarbeiroMapper barbeiroMapper;

    @Autowired
    private ServicoMapper servicoMapper;

    public Agendamento toEntity(AgendamentoRequestDTO dto) {
        if (dto == null) return null;

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.getDataHora());
        agendamento.setObservacoes(dto.getObservacoes());
        agendamento.setStatus("AGENDADO");

        // Os relacionamentos (cliente, barbeiro, servico) devem ser setados no service
        // usando os IDs do DTO

        return agendamento;
    }

    public AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        if (agendamento == null) return null;

        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(agendamento.getId());
        dto.setDataHora(agendamento.getDataHora());
        dto.setStatus(agendamento.getStatus());
        dto.setObservacoes(agendamento.getObservacoes());

        // Mapear relacionamentos
        if (agendamento.getCliente() != null) {
            dto.setCliente(clienteMapper.toResponseDTO(agendamento.getCliente()));
        }

        if (agendamento.getBarbeiro() != null) {
            dto.setBarbeiro(barbeiroMapper.toResponseDTO(agendamento.getBarbeiro()));
        }

        if (agendamento.getServico() != null) {
            dto.setServico(servicoMapper.toResponseDTO(agendamento.getServico()));
        }

        return dto;
    }

    public void updateEntityFromDTO(AgendamentoRequestDTO dto, Agendamento agendamento) {
        if (dto == null || agendamento == null) return;

        agendamento.setDataHora(dto.getDataHora());
        agendamento.setObservacoes(dto.getObservacoes());
        // Os relacionamentos devem ser atualizados no service
    }
}