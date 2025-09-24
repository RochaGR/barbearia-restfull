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
public class ClienteRegistroDTO {

    // Dados do Usuario
    @NotBlank(message = "Email é obrigatório")
    @Size(min = 3, max = 50, message = "Email deve ter entre 3 e 50 caracteres")
    private String username;

    @NotBlank(message = "Senha é obrigatório")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;

    // Dados do Cliente
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    private String telefone;
}