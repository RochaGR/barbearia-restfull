package com.br.barbeariaRest.dto.request;

import com.br.barbeariaRest.model.ExcecaoHorarioBarbeiro;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcecaoHorarioRequestDTO {

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate dataFim;

    private LocalTime horaInicio;

    private LocalTime horaFim;

    @NotNull(message = "Tipo de exceção é obrigatório")
    private ExcecaoHorarioBarbeiro.TipoExcecao tipo;

    @Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
    private String observacao;
}