package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.HorarioTrabalhoBarbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioTrabalhoBarbeiroRepository extends JpaRepository<HorarioTrabalhoBarbeiro, Long> {
    List<HorarioTrabalhoBarbeiro> findByBarbeiroIdAndAtivoTrue(Long barbeiroId);

    Optional<HorarioTrabalhoBarbeiro> findByBarbeiroIdAndDiaSemanaAndAtivoTrue(Long barbeiroId, Integer diaSemana);

    List<HorarioTrabalhoBarbeiro> findByBarbeiroIdAndDiaSemanaInAndAtivoTrue(Long barbeiroId, List<Integer> diasSemana);
}