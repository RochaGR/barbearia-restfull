package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {
    Optional<CupomDesconto> findByCodigo(String codigo);

    @Query("SELECT c FROM CupomDesconto c WHERE c.cliente.id = :clienteId AND c.status = :status")
    List<CupomDesconto> findByClienteIdAndStatus(@Param("clienteId") Long clienteId, @Param("status") CupomDesconto.StatusCupom status);
}

