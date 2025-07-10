package com.banquito.core.examen.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Denominacion {
    private int valor;
    private int cantidad;
    private BigDecimal monto;
}