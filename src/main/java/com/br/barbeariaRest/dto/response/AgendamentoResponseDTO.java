package com.br.barbeariaRest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoResponseDTO {
    private Long id;
    private ClienteResponseDTO cliente;
    private BarbeiroResponseDTO barbeiro;
    private ServicoResponseDTO servico;
    private LocalDateTime dataHora;
    private String status;
    private String observacoes;

}