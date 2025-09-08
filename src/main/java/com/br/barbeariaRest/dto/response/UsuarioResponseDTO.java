package com.br.barbeariaRest.dto.response;

import com.br.barbeariaRest.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private Role role;
    private boolean ativo;
}