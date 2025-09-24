package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.response.BarbeiroResponseDTO;
import com.br.barbeariaRest.model.Barbeiro;
import org.springframework.stereotype.Component;

@Component
public class BarbeiroMapper {

    public static BarbeiroResponseDTO toDto(Barbeiro barbeiro) {
        if (barbeiro == null) return null;

        return new BarbeiroResponseDTO(
                barbeiro.getId(),
                barbeiro.getNome(),
                barbeiro.getEspecialidades(),
                barbeiro.getTelefone(),
                barbeiro.isAtivo()
        );
    }

    public static Barbeiro toEntity(BarbeiroResponseDTO barbeiroResponseDTO) {
        if (barbeiroResponseDTO == null) return null;

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setId(barbeiroResponseDTO.getId());
        barbeiro.setNome(barbeiroResponseDTO.getNome());
        barbeiro.setEspecialidades(barbeiroResponseDTO.getEspecialidades());
        barbeiro.setAtivo(barbeiroResponseDTO.isAtivo());

        return barbeiro;
    }
}