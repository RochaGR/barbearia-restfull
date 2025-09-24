package com.br.barbeariaRest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarbeiroResponseDTO {
    private Long id;
    private String nome;
    private String especialidades;
    private String telefone;
    private boolean ativo;

}