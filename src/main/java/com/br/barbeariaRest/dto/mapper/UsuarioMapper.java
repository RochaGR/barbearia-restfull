package com.br.barbeariaRest.dto.mapper;

import com.br.barbeariaRest.dto.request.UsuarioRequestDTO;
import com.br.barbeariaRest.dto.response.LoginResponseDTO;
import com.br.barbeariaRest.dto.response.UsuarioResponseDTO;
import com.br.barbeariaRest.enums.Role;
import com.br.barbeariaRest.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); // Será criptografada no service

        // Define role padrão como CLIENTE se não for fornecida
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            usuario.setRole(Role.valueOf(dto.getRole()));
        } else {
            usuario.setRole(Role.CLIENTE); // Valor padrão
        }

        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        dto.setAtivo(usuario.isAtivo());

        return dto;
    }

    public LoginResponseDTO toLoginResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setTipo("Bearer");
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        dto.setId(usuario.getId());

        return dto;
    }

    public void updateEntityFromDTO(UsuarioRequestDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) return;

        usuario.setEmail(dto.getEmail());

        // Atualiza senha apenas se for fornecida
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            usuario.setSenha(dto.getSenha()); // Será criptografada no service
        }

        // Atualiza role apenas se for fornecida
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            usuario.setRole(Role.valueOf(dto.getRole()));
        }
    }
}