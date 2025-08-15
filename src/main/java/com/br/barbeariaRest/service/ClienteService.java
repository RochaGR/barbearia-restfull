package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.Cliente;
import com.br.barbeariaRest.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElse(null);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();

    }

}
