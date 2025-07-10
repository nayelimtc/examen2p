package com.banquito.core.examen.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "transacciones_turnos")
public class TransaccionTurno {
    @Id
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private String tipoTransaccion; // INICIO, AHORRO, DEPOSITO, CIERRE
    private BigDecimal montoTotal;
    private List<Denominacion> denominaciones;
    private LocalDateTime fechaTransaccion;
    private String estado; // ACTIVO, INACTIVO
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private Long version;
} 