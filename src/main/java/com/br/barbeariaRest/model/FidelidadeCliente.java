package com.br.barbeariaRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fidelidade_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FidelidadeCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "cortes_realizados")
    private Integer cortesRealizados = 0;

    @Column(name = "total_cupons_gerados")
    private Integer totalCuponsGerados = 0;

    @Column(name = "total_economizado")
    private java.math.BigDecimal totalEconomizado = java.math.BigDecimal.ZERO;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

