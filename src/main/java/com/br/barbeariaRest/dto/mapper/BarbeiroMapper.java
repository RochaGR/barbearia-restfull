package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.request.BarbeiroRequestDTO;
import com.br.barbeariaRest.dto.response.BarbeiroResponseDTO;
import com.br.barbeariaRest.model.Barbeiro;
import org.springframework.stereotype.Component;

@Component
public class BarbeiroMapper {

    public Barbeiro toEntity(BarbeiroRequestDTO dto) {
        if (dto == null) return null;

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(dto.getNome());
        barbeiro.setEspecialidades(dto.getEspecialidades());
        barbeiro.setAtivo(dto.isAtivo());

        return barbeiro;
    }

    public BarbeiroResponseDTO toResponseDTO(Barbeiro barbeiro) {
        if (barbeiro == null) return null;

        BarbeiroResponseDTO dto = new BarbeiroResponseDTO();
        dto.setId(barbeiro.getId());
        dto.setNome(barbeiro.getNome());
        dto.setEspecialidades(barbeiro.getEspecialidades());
        dto.setAtivo(barbeiro.isAtivo());

        // Buscar email do usuário relacionado
        if (barbeiro.getUsuario() != null) {
            dto.setEmail(barbeiro.getUsuario().getEmail());
        }

        return dto;
    }

    public void updateEntityFromDTO(BarbeiroRequestDTO dto, Barbeiro barbeiro) {
        if (dto == null || barbeiro == null) return;

        barbeiro.setNome(dto.getNome());
        barbeiro.setEspecialidades(dto.getEspecialidades());
        barbeiro.setAtivo(dto.isAtivo());
    }
}