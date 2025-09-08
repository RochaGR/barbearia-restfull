package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.mapper.ClienteMapper;
import com.br.barbeariaRest.dto.request.ClienteRequestDTO;
import com.br.barbeariaRest.dto.request.UsuarioRequestDTO;
import com.br.barbeariaRest.dto.response.ClienteResponseDTO;
import com.br.barbeariaRest.model.Cliente;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.ClienteRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDTO findByUsuarioId(Long usuarioId) {
        Cliente cliente = clienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizarByUsuarioId(Long usuarioId, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        clienteMapper.updateEntityFromDTO(dto, cliente);
        Cliente atualizado = clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(atualizado);
    }

    public ClienteResponseDTO criar(Long usuarioId, ClienteRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setUsuario(usuario);

        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(salvo);
    }

    public ClienteResponseDTO criarCliente(Long usuarioId, UsuarioRequestDTO usuarioDTO) {
        // Cria o DTO do cliente com base nos dados do usuário
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO();
        clienteDTO.setNome(usuarioDTO.getNome()); // CORRETO: pega do usuarioDTO

        // Copia o telefone se existir, senão deixa como string vazia
        clienteDTO.setTelefone(usuarioDTO.getTelefone() != null ? usuarioDTO.getTelefone() : "");

        return criar(usuarioId, clienteDTO);
    }

    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }
}