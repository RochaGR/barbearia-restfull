package com.br.barbeariaRest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Barbeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;
    private String especialidades;
    private String telefone;
    private boolean ativo;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}

