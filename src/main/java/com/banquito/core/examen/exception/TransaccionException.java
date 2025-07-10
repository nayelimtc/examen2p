package com.banquito.core.examen.exception;

public class TransaccionException extends RuntimeException {

    private final String operation;
    private final String reason;

    public TransaccionException(String operation, String reason) {
        super();
        this.operation = operation;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return "Error en la transacción: " + this.operation + ". Motivo: " + this.reason;
    }
} 