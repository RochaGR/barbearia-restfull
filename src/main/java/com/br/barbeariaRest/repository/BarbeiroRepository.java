package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbeiroRepository  extends JpaRepository<Barbeiro, Integer> {

}
