package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbeiroRepository  extends JpaRepository<Cliente, Integer> {
}
