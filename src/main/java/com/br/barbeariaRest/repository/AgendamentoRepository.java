package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByClienteId(Long clienteId);
    List<Agendamento> findByBarbeiroId(Long barbeiroId);
    List<Agendamento> findByClienteUsuarioId(Long usuarioId);
    List<Agendamento> findByBarbeiroUsuarioId(Long usuarioId);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByStatus(String status);
    Optional<Agendamento> findById(Long id);
    List<Agendamento> findByBarbeiroIdAndDataHoraBetween(Long barbeiroId, LocalDateTime inicio, LocalDateTime fim);


}