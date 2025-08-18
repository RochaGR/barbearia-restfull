package com.br.barbeariaRest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String especialidade;
    private boolean ativo;

    public Barbeiro() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Barbeiro(int id, boolean ativo, String especialidade, String nome) {
        this.id = id;
        this.ativo = ativo;
        this.especialidade = especialidade;
        this.nome = nome;
    }
}
