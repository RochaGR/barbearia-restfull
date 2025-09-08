package com.br.barbeariaRest.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequestDTO {

    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome; // REMOVIDO: @NotBlank

    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    private String telefone; // REMOVIDO: @NotBlank

    private Long usuarioId;
}