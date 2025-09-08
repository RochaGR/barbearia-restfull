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
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private String email;
    private Role role;
    private Long id;

}