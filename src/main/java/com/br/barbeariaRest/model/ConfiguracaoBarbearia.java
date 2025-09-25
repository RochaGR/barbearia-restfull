package com.br.barbeariaRest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "configuracao_barbearia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoBarbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hora_abertura")
    private LocalTime horaAbertura;

    @Column(name = "hora_fechamento")
    private LocalTime horaFechamento;

    @Column(name = "intervalo_agendamento_minutos")
    private Integer intervaloAgendamentoMinutos = 30; // Intervalos de 30min

    @Column(name = "antecedencia_minima_horas")
    private Integer antecedenciaMinimaHoras = 2; // Mín 2h de antecedência

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Dias da semana que funciona (1=Segunda, 7=Domingo)
    @ElementCollection
    @CollectionTable(name = "dias_funcionamento",
            joinColumns = @JoinColumn(name = "configuracao_id"))
    @Column(name = "dia_semana")
    private Set<Integer> diasFuncionamento = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}