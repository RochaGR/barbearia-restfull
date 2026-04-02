package com.br.barbeariaRest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupom_desconto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupomDesconto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(unique = true)
    private String codigo;

    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto;

    @Enumerated(EnumType.STRING)
    private StatusCupom status = StatusCupom.ATIVO;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @ManyToOne
    @JoinColumn(name = "agendamento_uso_id")
    private Agendamento agendamentoUso;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum StatusCupom {
        ATIVO,
        USADO,
        EXPIRADO
    }
}

