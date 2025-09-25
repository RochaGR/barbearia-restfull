package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.request.BarbeiroRegistroDTO;
import com.br.barbeariaRest.dto.response.BarbeiroResponseDTO;
import com.br.barbeariaRest.dto.mapper.BarbeiroMapper;
import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.model.Role;
import com.br.barbeariaRest.model.Usuario;

import com.br.barbeariaRest.repository.BarbeiroRepository;
import com.br.barbeariaRest.repository.RoleRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BarbeiroService {

    @Autowired
    private BarbeiroRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public BarbeiroResponseDTO salvar(BarbeiroResponseDTO dto) {
        Barbeiro barbeiro = BarbeiroMapper.toEntity(dto);
        Barbeiro salvo = repository.save(barbeiro);
        return BarbeiroMapper.toDto(salvo);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public BarbeiroResponseDTO buscarPorId(Long id) {
        Optional<Barbeiro> barbeiro = repository.findById(id);
        return barbeiro.map(BarbeiroMapper::toDto).orElse(null);
    }

    public List<BarbeiroResponseDTO> buscarTodos() {
        List<Barbeiro> barbeiros = repository.findAll();
        return barbeiros.stream().map(BarbeiroMapper::toDto).toList();
    }

    @Transactional
    public BarbeiroResponseDTO registrarBarbeiro(BarbeiroRegistroDTO dto) {

        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe!");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role barbeiroRole = roleRepository.findByNome("BARBEIRO")
                .orElseThrow(() -> new RuntimeException("Role BARBEIRO não encontrada"));
        usuario.setRoles(Set.of(barbeiroRole));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(dto.getNome());
        barbeiro.setEspecialidades(dto.getEspecialidades());
        barbeiro.setTelefone(dto.getTelefone());
        barbeiro.setAtivo(dto.isAtivo());
        barbeiro.setUsuario(usuarioSalvo);

        Barbeiro barbeiroSalvo = repository.save(barbeiro);
        return BarbeiroMapper.toDto(barbeiroSalvo);
    }

    public BarbeiroResponseDTO buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(Usuario::getBarbeiro)
                .map(BarbeiroMapper::toDto)
                .orElse(null);
    }


    //Verifica se o usuário logado é proprietário do barbeiro
    //Usado para autorização em @PreAuthorize

    public boolean isOwner(Long barbeiroId, String username) {
        return usuarioRepository.findByUsername(username)
                .map(Usuario::getBarbeiro)
                .map(Barbeiro::getId)
                .map(id -> id.equals(barbeiroId))
                .orElse(false);
    }
}