package com.br.barbeariaRest.exception;

public class HorarioIndisponivelException extends RuntimeException {
    public HorarioIndisponivelException(String message) {
        super(message);
    }
}