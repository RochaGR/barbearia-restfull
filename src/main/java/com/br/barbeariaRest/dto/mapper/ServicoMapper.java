package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.request.ServicoRequestDTO;
import com.br.barbeariaRest.dto.response.ServicoResponseDTO;
import com.br.barbeariaRest.model.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public Servico toEntity(ServicoRequestDTO dto) {
        if (dto == null) return null;

        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());
        servico.setAtivo(dto.isAtivo());

        return servico;
    }

    public ServicoResponseDTO toResponseDTO(Servico servico) {
        if (servico == null) return null;

        ServicoResponseDTO dto = new ServicoResponseDTO();
        dto.setId(servico.getId());
        dto.setNome(servico.getNome());
        dto.setDescricao(servico.getDescricao());
        dto.setPreco(servico.getPreco());
        dto.setDuracaoMinutos(servico.getDuracaoMinutos());
        dto.setAtivo(servico.isAtivo());

        return dto;
    }

    public void updateEntityFromDTO(ServicoRequestDTO dto, Servico servico) {
        if (dto == null || servico == null) return;

        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());
        servico.setAtivo(dto.isAtivo());
    }
}