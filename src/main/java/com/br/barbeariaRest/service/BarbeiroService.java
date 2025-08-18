package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.repository.BarbeiroRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    private BarbeiroService(BarbeiroRepository barbeiroRepository) {
        this.barbeiroRepository = barbeiroRepository;
    }

    ;

    public Barbeiro salvar(Barbeiro barbeiro) {
        return barbeiroRepository.save(barbeiro);

    }

    public Barbeiro buscarPorId(Integer id) {
        return barbeiroRepository.findById(id).orElse(null);

    }

    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll();

    }

    public void excluir (Integer id) {
        barbeiroRepository.deleteById(id);
    }


}
