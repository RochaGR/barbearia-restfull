package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.FidelidadeCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FidelidadeClienteRepository extends JpaRepository<FidelidadeCliente, Long> {
    @Query("SELECT f FROM FidelidadeCliente f WHERE f.cliente.id = :clienteId")
    Optional<FidelidadeCliente> findByClienteId(@Param("clienteId") Long clienteId);
}

