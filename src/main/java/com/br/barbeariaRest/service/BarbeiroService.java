package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.repository.BarbeiroRepository;
import com.br.barbeariaRest.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BarbeiroService {

    @Autowired
    private BarbeiroRepository  barbeiroRepository;

    public Barbeiro salvar(Barbeiro barbeiro){
        return barbeiroRepository.save(barbeiro);

    }

    public Barbeiro buscarPorId(Integer id) {
        return barbeiroRepository.findById(id).orElse(null);

    }

    public List<Barbeiro> listarTodos(){
        return barbeiroRepository.findAll();

    }


}
