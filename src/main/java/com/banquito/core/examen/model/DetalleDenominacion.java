package com.banquito.core.examen.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DetalleDenominacion {
    
    public static final String DENOMINACION_UNO = "1";
    public static final String DENOMINACION_CINCO = "5";
    public static final String DENOMINACION_DIEZ = "10";
    public static final String DENOMINACION_VEINTE = "20";
    public static final String DENOMINACION_CINCUENTA = "50";
    public static final String DENOMINACION_CIEN = "100";
    

    private static final Map<String, BigDecimal> VALORES_DENOMINACION = new HashMap<>();
    
    static {
        VALORES_DENOMINACION.put(DENOMINACION_UNO, new BigDecimal("1"));
        VALORES_DENOMINACION.put(DENOMINACION_CINCO, new BigDecimal("5"));
        VALORES_DENOMINACION.put(DENOMINACION_DIEZ, new BigDecimal("10"));
        VALORES_DENOMINACION.put(DENOMINACION_VEINTE, new BigDecimal("20"));
        VALORES_DENOMINACION.put(DENOMINACION_CINCUENTA, new BigDecimal("50"));
        VALORES_DENOMINACION.put(DENOMINACION_CIEN, new BigDecimal("100"));
    }
    
    private String denominacion;
    private Integer cantidad;
    
    public DetalleDenominacion(String denominacion) {
        this.denominacion = denominacion;
        this.cantidad = 0;
    }
    
    public DetalleDenominacion(String denominacion, Integer cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
    }
    
    public BigDecimal getValorTotal() {
        BigDecimal valorUnitario = VALORES_DENOMINACION.get(denominacion);
        if (valorUnitario == null) {
            throw new IllegalArgumentException("Denominación no válida: " + denominacion);
        }
        return valorUnitario.multiply(new BigDecimal(cantidad));
    }
    
    public static BigDecimal getValorDenominacion(String denominacion) {
        return VALORES_DENOMINACION.get(denominacion);
    }
    
    public static boolean esValidaDenominacion(String denominacion) {
        return VALORES_DENOMINACION.containsKey(denominacion);
    }
} 