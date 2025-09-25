package com.br.barbeariaRest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private String code;
    private Map<String, String> errors;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ValidationErrorResponse(String code, Map<String, String> errors) {
        this.code = code;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}