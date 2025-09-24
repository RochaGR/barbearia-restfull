package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.request.ServicoRequestDTO;
import com.br.barbeariaRest.dto.response.ServicoResponseDTO;
import com.br.barbeariaRest.dto.mapper.ServicoMapper;
import com.br.barbeariaRest.model.Servico;
import com.br.barbeariaRest.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    public ServicoResponseDTO criar(ServicoRequestDTO dto) {
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());
        servico.setAtivo(dto.isAtivo());

        Servico servicoSalvo = repository.save(servico);
        return ServicoMapper.toDto(servicoSalvo);
    }

    public ServicoResponseDTO atualizar(Long id, ServicoRequestDTO dto) {
        Optional<Servico> servicoExistente = repository.findById(id);
        if (servicoExistente.isEmpty()) {
            throw new RuntimeException("Serviço não encontrado");
        }

        Servico servico = servicoExistente.get();
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());
        servico.setAtivo(dto.isAtivo());

        Servico servicoSalvo = repository.save(servico);
        return ServicoMapper.toDto(servicoSalvo);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Serviço não encontrado");
        }
        repository.deleteById(id);
    }

    public ServicoResponseDTO buscarPorId(Long id) {
        Optional<Servico> servico = repository.findById(id);
        return servico.map(ServicoMapper::toDto).orElse(null);
    }

    public List<ServicoResponseDTO> buscarTodos() {
        List<Servico> servicos = repository.findAll();
        return servicos.stream().map(ServicoMapper::toDto).toList();
    }

    public List<ServicoResponseDTO> buscarAtivos() {
        List<Servico> servicos = repository.findAll();
        return servicos.stream()
                .filter(Servico::isAtivo)
                .map(ServicoMapper::toDto)
                .toList();
    }

    public ServicoResponseDTO ativarDesativar(Long id) {
        Optional<Servico> servicoExistente = repository.findById(id);
        if (servicoExistente.isEmpty()) {
            throw new RuntimeException("Serviço não encontrado");
        }

        Servico servico = servicoExistente.get();
        servico.setAtivo(!servico.isAtivo());

        Servico servicoSalvo = repository.save(servico);
        return ServicoMapper.toDto(servicoSalvo);
    }
}