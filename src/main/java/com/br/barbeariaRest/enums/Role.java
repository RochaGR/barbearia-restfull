package com.br.barbeariaRest.enums;

public enum Role {
    CLIENTE("Cliente"),
    BARBEIRO("Barbeiro"),
    ADMIN("Administrador");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}