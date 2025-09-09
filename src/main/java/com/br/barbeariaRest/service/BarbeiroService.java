package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.mapper.BarbeiroMapper;
import com.br.barbeariaRest.dto.request.BarbeiroRequestDTO;
import com.br.barbeariaRest.dto.request.UsuarioRequestDTO;
import com.br.barbeariaRest.dto.response.BarbeiroResponseDTO;
import com.br.barbeariaRest.dto.response.UsuarioResponseDTO;
import com.br.barbeariaRest.enums.Role;
import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.BarbeiroRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final UsuarioRepository usuarioRepository;
    private final BarbeiroMapper barbeiroMapper;
    private final AuthService authService;

    public List<BarbeiroResponseDTO> findAllAtivos() {
        return barbeiroRepository.findByAtivoTrue()
                .stream()
                .map(barbeiroMapper::toResponseDTO)
                .toList();
    }

    public BarbeiroResponseDTO findByUsuarioId(Long usuarioId) {
        Barbeiro barbeiro = barbeiroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
        return barbeiroMapper.toResponseDTO(barbeiro);
    }

    public BarbeiroResponseDTO atualizarByUsuarioId(Long usuarioId, BarbeiroRequestDTO dto) {
        Barbeiro barbeiro = barbeiroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        barbeiroMapper.updateEntityFromDTO(dto, barbeiro);
        Barbeiro atualizado = barbeiroRepository.save(barbeiro);

        return barbeiroMapper.toResponseDTO(atualizado);
    }

    public BarbeiroResponseDTO findById(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
        return barbeiroMapper.toResponseDTO(barbeiro);
    }

    @Transactional
    public BarbeiroResponseDTO criarBarbeiro(BarbeiroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        UsuarioRequestDTO usuarioDTO = new UsuarioRequestDTO();
        usuarioDTO.setEmail(dto.getEmail());
        usuarioDTO.setSenha(dto.getSenha());
        usuarioDTO.setRole(Role.BARBEIRO.name());
        usuarioDTO.setNome(dto.getNome());

        UsuarioResponseDTO usuarioCriado = authService.registrar(usuarioDTO);

        Usuario usuario = usuarioRepository.findById(usuarioCriado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado após criação"));

        Barbeiro barbeiro = barbeiroMapper.toEntity(dto);
        barbeiro.setUsuario(usuario);
        barbeiro.setAtivo(true);

        Barbeiro barbeiroSalvo = barbeiroRepository.save(barbeiro);

        return barbeiroMapper.toResponseDTO(barbeiroSalvo);
    }


    public List<BarbeiroResponseDTO> findAll() {
        return barbeiroRepository.findAll()
                .stream()
                .map(barbeiroMapper::toResponseDTO)
                .toList();
    }

}