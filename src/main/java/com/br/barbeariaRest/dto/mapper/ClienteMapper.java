package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.response.ClienteResponseDTO;
import com.br.barbeariaRest.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public static ClienteResponseDTO toDto(Cliente cliente) {
        if (cliente == null) return null;

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone()
        );
    }

    public static Cliente toEntity(ClienteResponseDTO clienteResponseDTO) {
        if (clienteResponseDTO == null) return null;

        Cliente cliente = new Cliente();
        cliente.setId(clienteResponseDTO.getId());
        cliente.setNome(clienteResponseDTO.getNome());

        return cliente;
    }
}