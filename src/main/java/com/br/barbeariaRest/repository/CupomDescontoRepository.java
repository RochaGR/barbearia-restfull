package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {
    Optional<CupomDesconto> findByCodigo(String codigo);

    List<CupomDesconto> findByClienteIdAndStatus(Long clienteId, CupomDesconto.StatusCupom status);
}

