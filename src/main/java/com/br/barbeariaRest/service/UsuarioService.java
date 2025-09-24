package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.response.UsuarioResponseDTO;
import com.br.barbeariaRest.model.Role;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.RoleRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario salvar(UsuarioResponseDTO dto) {
        Usuario user = new Usuario();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Set<Role> roles = dto.getRoles().stream()
                .map(nome -> roleRepository.findByNome(nome).orElseThrow())
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return usuarioRepository.save(user);
    }

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setUsername(usuario.getUsername());
            dto.setRoles(usuario.getRoles().stream()
                    .map(Role::getNome)
                    .collect(Collectors.toSet()));
            return dto;
        }).collect(Collectors.toList());
    }
}