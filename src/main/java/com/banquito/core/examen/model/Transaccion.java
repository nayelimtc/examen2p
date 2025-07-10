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

@Document(collection = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaccion {
    
    // Constantes para los tipos de transacción válidos
    public static final String TIPO_RETIRO = "RETIRO";
    public static final String TIPO_DEPOSITO = "DEPOSITO";
    
    @Id
    @EqualsAndHashCode.Include
    private String id;
    
    private String codigoTransaccion;
    private String turnoId;
    private String cajeroId;
    private String codigoCajero;
    private String codigoCaja;
    private String tipo;
    private BigDecimal monto;
    private List<DetalleDenominacion> denominaciones;
    private String clienteId;
    private String numeroCuenta;
    private LocalDateTime fechaTransaccion;
    private String observaciones;
    
    public Transaccion(String id) {
        this.id = id;
    }
    
    public BigDecimal calcularMontoTotal() {
        if (denominaciones == null) return BigDecimal.ZERO;
        return denominaciones.stream()
                .map(DetalleDenominacion::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public static boolean esValidoTipo(String tipo) {
        return TIPO_RETIRO.equals(tipo) || TIPO_DEPOSITO.equals(tipo);
    }
} 