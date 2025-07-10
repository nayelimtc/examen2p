package com.banquito.core.examen.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "turnos")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Turno {
    
    // Constantes para los estados válidos
    public static final String ESTADO_ABIERTO = "ABIERTO";
    public static final String ESTADO_CERRADO = "CERRADO";
    
    @Id
    @EqualsAndHashCode.Include
    private String id;
    
    private String codigoTurno;
    private String cajeroId;
    private String codigoCajero;
    private String codigoCaja;
    private String agencia;
    private String estado;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private List<DetalleDenominacion> dineroInicial;
    private List<DetalleDenominacion> dineroFinal;
    private BigDecimal montoCalculado;
    private BigDecimal montoDeclarado;
    private BigDecimal diferencia;
    private Boolean tieneAlerta;
    private String observaciones;
    
    public Turno(String id) {
        this.id = id;
    }
    
    public BigDecimal calcularMontoInicial() {
        if (dineroInicial == null) return BigDecimal.ZERO;
        return dineroInicial.stream()
                .map(DetalleDenominacion::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calcularMontoFinal() {
        if (dineroFinal == null) return BigDecimal.ZERO;
        return dineroFinal.stream()
                .map(DetalleDenominacion::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public static boolean esValidoEstado(String estado) {
        return ESTADO_ABIERTO.equals(estado) || ESTADO_CERRADO.equals(estado);
    }
} 