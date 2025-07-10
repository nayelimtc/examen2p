package com.banquito.core.examen.exception;

public class TurnoException extends RuntimeException {

    private final String operation;
    private final String reason;

    public TurnoException(String operation, String reason) {
        super();
        this.operation = operation;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return "Error en la operación: " + this.operation + ". Motivo: " + this.reason;
    }
} 