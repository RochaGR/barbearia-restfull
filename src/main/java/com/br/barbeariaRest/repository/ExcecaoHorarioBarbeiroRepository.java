package com.br.barbeariaRest.repository;

import com.br.barbeariaRest.model.ExcecaoHorarioBarbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExcecaoHorarioBarbeiroRepository extends JpaRepository<ExcecaoHorarioBarbeiro, Long> {

    List<ExcecaoHorarioBarbeiro> findByBarbeiroIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqualAndAtivoTrue(
            Long barbeiroId, LocalDate dataFim, LocalDate dataInicio);

    @Query("SELECT e FROM ExcecaoHorarioBarbeiro e WHERE e.barbeiro.id = :barbeiroId " +
            "AND e.ativo = true " +
            "AND :data BETWEEN e.dataInicio AND e.dataFim")
    List<ExcecaoHorarioBarbeiro> findExcecoesPorData(@Param("barbeiroId") Long barbeiroId,
                                                     @Param("data") LocalDate data);

    @Query("SELECT e FROM ExcecaoHorarioBarbeiro e WHERE e.barbeiro.id = :barbeiroId " +
            "AND e.ativo = true " +
            "AND e.dataInicio <= :dataFim " +
            "AND e.dataFim >= :dataInicio")
    List<ExcecaoHorarioBarbeiro> findExcecoesNoPeriodo(@Param("barbeiroId") Long barbeiroId,
                                                       @Param("dataInicio") LocalDate dataInicio,
                                                       @Param("dataFim") LocalDate dataFim);
}