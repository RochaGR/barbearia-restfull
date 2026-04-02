package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.FidelidadeCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FidelidadeClienteRepository extends JpaRepository<FidelidadeCliente, Long> {
    Optional<FidelidadeCliente> findByClienteId(Long clienteId);
}

