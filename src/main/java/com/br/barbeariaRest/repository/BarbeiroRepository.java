package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    Optional<Barbeiro> findById(Long id);
}