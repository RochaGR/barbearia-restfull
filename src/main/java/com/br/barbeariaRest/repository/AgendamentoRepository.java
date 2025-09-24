package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByClienteId(Long clienteId);

    List<Agendamento> findByBarbeiroId(Long barbeiroId);

    List<Agendamento> findByStatus(String status);

    // Buscar agendamentos de um período
    @Query("SELECT a FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim")
    List<Agendamento> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio,
                                            @Param("fim") LocalDateTime fim);

    // Verificar conflito de horário para barbeiro
    @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
            "AND a.dataHora BETWEEN :inicio AND :fim " +
            "AND a.status NOT IN ('CANCELADO') " +
            "AND (:agendamentoId IS NULL OR a.id != :agendamentoId)")
    List<Agendamento> findConflitosHorario(@Param("barbeiroId") Long barbeiroId,
                                           @Param("inicio") LocalDateTime inicio,
                                           @Param("fim") LocalDateTime fim,
                                           @Param("agendamentoId") Long agendamentoId);
}