package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.ConfiguracaoBarbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoBarbeariaRepository extends JpaRepository<ConfiguracaoBarbearia, Long> {
    Optional<ConfiguracaoBarbearia> findByAtivoTrue();
}