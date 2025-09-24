package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.response.AgendamentoResponseDTO;
import com.br.barbeariaRest.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public static AgendamentoResponseDTO toDto(Agendamento agendamento) {
        if (agendamento == null) return null;

        return new AgendamentoResponseDTO(
                agendamento.getId(),
                ClienteMapper.toDto(agendamento.getCliente()),
                BarbeiroMapper.toDto(agendamento.getBarbeiro()),
                ServicoMapper.toDto(agendamento.getServico()),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                agendamento.getObservacoes()
        );
    }

    public static Agendamento toEntity(AgendamentoResponseDTO agendamentoResponseDTO) {
        if (agendamentoResponseDTO == null) return null;

        Agendamento agendamento = new Agendamento();
        agendamento.setId(agendamentoResponseDTO.getId());
        agendamento.setCliente(ClienteMapper.toEntity(agendamentoResponseDTO.getCliente()));
        agendamento.setBarbeiro(BarbeiroMapper.toEntity(agendamentoResponseDTO.getBarbeiro()));
        agendamento.setServico(ServicoMapper.toEntity(agendamentoResponseDTO.getServico()));
        agendamento.setDataHora(agendamentoResponseDTO.getDataHora());
        agendamento.setStatus(agendamentoResponseDTO.getStatus());
        agendamento.setObservacoes(agendamentoResponseDTO.getObservacoes());

        return agendamento;
    }
}