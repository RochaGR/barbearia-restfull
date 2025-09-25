package com.br.barbeariaRest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioTrabalhoRequestDTO {

    @NotNull(message = "Dia da semana é obrigatório")
    @Min(value = 1, message = "Dia da semana deve ser entre 1 (Segunda) e 7 (Domingo)")
    @Max(value = 7, message = "Dia da semana deve ser entre 1 (Segunda) e 7 (Domingo)")
    private Integer diaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    private LocalTime pausaInicio;

    private LocalTime pausaFim;

    private Boolean ativo = true;
}