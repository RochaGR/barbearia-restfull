package com.br.barbeariaRest.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Barbeiro ID é obrigatório")
    private Long barbeiroId;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser futuras")
    private LocalDateTime dataHora;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;
}