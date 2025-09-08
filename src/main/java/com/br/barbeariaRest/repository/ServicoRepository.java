package com.br.barbeariaRest.repository;


import com.br.barbeariaRest.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository  extends JpaRepository<Servico, Long> {


}

