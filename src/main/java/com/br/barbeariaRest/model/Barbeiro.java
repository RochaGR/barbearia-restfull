package com.br.barbeariaRest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String especialidades;
    private boolean ativo;
    // Deixei para quando criar vir false, por regras de negocio, no front terá a opção de selecionar
    //se ele vai estar ativo ou não




}
