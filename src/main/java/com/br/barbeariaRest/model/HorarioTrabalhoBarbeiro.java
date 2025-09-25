package com.br.barbeariaRest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "horario_trabalho_barbeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioTrabalhoBarbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id")
    private Barbeiro barbeiro;

    @Column(name = "dia_semana")
    private Integer diaSemana;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "pausa_inicio")
    private LocalTime pausaInicio;

    @Column(name = "pausa_fim")
    private LocalTime pausaFim;
}