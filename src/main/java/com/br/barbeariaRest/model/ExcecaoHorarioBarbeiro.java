package com.br.barbeariaRest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "excecao_horario_barbeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcecaoHorarioBarbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id")
    private Barbeiro barbeiro;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoExcecao tipo;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public enum TipoExcecao {
        FOLGA,
        FERIAS,
        HORARIO_ESPECIAL,
        DISPONIVEL
    }
}