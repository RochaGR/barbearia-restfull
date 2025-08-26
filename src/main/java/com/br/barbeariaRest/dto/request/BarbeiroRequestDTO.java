package com.br.barbeariaRest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BarbeiroRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Especialidades não pode exceder 500 caracteres")
    private String especialidades;

    private boolean ativo = false;

    public BarbeiroRequestDTO() {}

    public BarbeiroRequestDTO(String nome, String especialidades, boolean ativo) {
        this.nome = nome;
        this.especialidades = especialidades;
        this.ativo = ativo;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecialidades() { return especialidades; }
    public void setEspecialidades(String especialidades) { this.especialidades = especialidades; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}