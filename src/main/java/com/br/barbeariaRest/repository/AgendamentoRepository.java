package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    List<Agendamento> findByClienteId(int clienteId);
    List<Agendamento> findByBarbeiroId(int barbeiroId);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByStatus(String status);

}

