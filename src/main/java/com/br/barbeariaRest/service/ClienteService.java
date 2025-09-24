package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.request.ClienteRegistroDTO;
import com.br.barbeariaRest.dto.response.ClienteResponseDTO;
import com.br.barbeariaRest.dto.mapper.ClienteMapper;
import com.br.barbeariaRest.model.Cliente;
import com.br.barbeariaRest.model.Role;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.ClienteRepository;
import com.br.barbeariaRest.repository.RoleRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClienteResponseDTO salvar(ClienteResponseDTO dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente salvo = repository.save(cliente);
        return ClienteMapper.toDto(salvo);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Optional<Cliente> cliente = repository.findById(id);
        return cliente.map(ClienteMapper::toDto).orElse(null);
    }

    public List<ClienteResponseDTO> buscarTodos() {
        List<Cliente> clientes = repository.findAll();
        return clientes.stream().map(ClienteMapper::toDto).toList();
    }

    @Transactional
    public ClienteResponseDTO registrarCliente(ClienteRegistroDTO dto) {
        // Verificar se username já existe
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe!");
        }

        // Criar Usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Adicionar role CLIENTE
        Role clienteRole = roleRepository.findByNome("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Role CLIENTE não encontrada"));
        usuario.setRoles(Set.of(clienteRole));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Criar Cliente
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        cliente.setUsuario(usuarioSalvo);

        Cliente clienteSalvo = repository.save(cliente);
        return ClienteMapper.toDto(clienteSalvo);
    }

    public ClienteResponseDTO buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(Usuario::getCliente)
                .map(ClienteMapper::toDto)
                .orElse(null);
    }
}