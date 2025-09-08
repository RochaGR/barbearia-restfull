package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.request.ClienteRequestDTO;
import com.br.barbeariaRest.dto.response.ClienteResponseDTO;
import com.br.barbeariaRest.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) return null;

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());

        return cliente;
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) return null;

        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());

        // Buscar email do usuário relacionado
        if (cliente.getUsuario() != null) {
            dto.setEmail(cliente.getUsuario().getEmail());
        }

        return dto;
    }

    public void updateEntityFromDTO(ClienteRequestDTO dto, Cliente cliente) {
        if (dto == null || cliente == null) return;

        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
    }
}