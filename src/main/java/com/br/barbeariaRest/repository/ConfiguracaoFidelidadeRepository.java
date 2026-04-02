package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.ConfiguracaoFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracaoFidelidadeRepository extends JpaRepository<ConfiguracaoFidelidade, Long> {
    Optional<ConfiguracaoFidelidade> findByAtivoTrue();
}

