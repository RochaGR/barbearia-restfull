package com.br.barbeariaRest.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarbeiroRegistroDTO {

    // Dados do Usuario
    @NotBlank(message = "Email é obrigatório")
    @Size(min = 3, max = 50, message = "Email deve ter entre 3 e 50 caracteres")
    private String username;

    @NotBlank(message = "Senha é obrigatório")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;

    // Dados do Barbeiro
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 255, message = "Especialidades deve ter no máximo 255 caracteres")
    private String especialidades;

    private String telefone;

    private boolean ativo = true;
}