package com.br.barbeariaRest.dto.mapper;


import com.br.barbeariaRest.dto.response.ServicoResponseDTO;
import com.br.barbeariaRest.model.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public static ServicoResponseDTO toDto(Servico servico) {
        if (servico == null) return null;
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracaoMinutos(),
                servico.isAtivo()
        );

    }
    public static Servico toEntity(ServicoResponseDTO servicoResponseDTO) {
        if (servicoResponseDTO == null) return null;
        Servico servico = new Servico();
        servico.setId(servicoResponseDTO.getId());
        servico.setNome(servicoResponseDTO.getNome());
        servico.setDescricao(servicoResponseDTO.getDescricao());
        servico.setPreco(servicoResponseDTO.getPreco());
        servico.setDuracaoMinutos(servicoResponseDTO.getDuracaoMinutos());
        servico.setAtivo(servicoResponseDTO.isAtivo());
        return servico;

    }

}