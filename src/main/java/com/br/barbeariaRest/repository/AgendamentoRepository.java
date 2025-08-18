package com.br.barbeariaRest.repository;


import com.br.barbeariaRest.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository  extends JpaRepository<Agendamento, Integer>{

}


