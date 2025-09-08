package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.mapper.ServicoMapper;
import com.br.barbeariaRest.dto.request.ServicoRequestDTO;
import com.br.barbeariaRest.dto.response.ServicoResponseDTO;
import com.br.barbeariaRest.model.Servico;
import com.br.barbeariaRest.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;

    public List<ServicoResponseDTO> findAll() {
        return servicoRepository.findAll()
                .stream()
                .map(servicoMapper::toResponseDTO)
                .toList();
    }

    public ServicoResponseDTO findById(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        return servicoMapper.toResponseDTO(servico);
    }

    public ServicoResponseDTO create(ServicoRequestDTO dto) {
        Servico servico = servicoMapper.toEntity(dto);
        Servico salvo = servicoRepository.save(servico);
        return servicoMapper.toResponseDTO(salvo);
    }

    public ServicoResponseDTO update(Long id, ServicoRequestDTO dto) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        servicoMapper.updateEntityFromDTO(dto, servico);
        Servico atualizado = servicoRepository.save(servico);

        return servicoMapper.toResponseDTO(atualizado);
    }

    public void delete(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new RuntimeException("Serviço não encontrado");
        }
        servicoRepository.deleteById(id);
    }
}