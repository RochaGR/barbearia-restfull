package com.br.barbeariaRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracao_fidelidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoFidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cortes_para_recompensa")
    private Integer cortesParaRecompensa = 5;

    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto = new BigDecimal("40.00");

    @Column(name = "validade_cupom_dias")
    private Integer validadeCupomDias = 30;

    private Boolean ativo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

