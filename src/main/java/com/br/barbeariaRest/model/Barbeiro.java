package com.br.barbeariaRest.model;

import jakarta.persistence.*;

@Entity
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

    public Barbeiro() {
    }

    public Barbeiro(Long id, String nome, String especialidades, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.especialidades = especialidades;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


}
