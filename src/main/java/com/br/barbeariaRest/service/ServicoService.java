package com.br.barbeariaRest.service;


import com.br.barbeariaRest.model.Servico;
import com.br.barbeariaRest.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(Integer id) {
        return servicoRepository.findById(id).orElse(null);

    }
    public List<Servico> buscarTodos() {
        return servicoRepository.findAll();
    }
    public void excluir(Integer id) {
        servicoRepository.deleteById(id);
    }

}
