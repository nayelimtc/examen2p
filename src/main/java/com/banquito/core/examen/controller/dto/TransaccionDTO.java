package com.banquito.core.examen.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Información de una transacción")
public class TransaccionDTO {
    
    @Schema(description = "ID de la transacción", example = "507f1f77bcf86cd799439013")
    private String id;
    
    @Schema(description = "Código de la transacción", example = "TXN-CAJ001-20240101-080015")
    private String codigoTransaccion;
    
    @Schema(description = "ID del turno", example = "507f1f77bcf86cd799439011")
    private String turnoId;
    
    @Schema(description = "ID del cajero", example = "507f1f77bcf86cd799439012")
    private String cajeroId;
    
    @Schema(description = "Código del cajero", example = "CAJ001")
    private String codigoCajero;
    
    @Schema(description = "Código de la caja", example = "CAJA01")
    private String codigoCaja;
    
    @Schema(description = "Tipo de transacción", example = "RETIRO", allowableValues = {"RETIRO", "DEPOSITO"})
    private String tipo;
    
    @Schema(description = "Monto de la transacción", example = "100.00")
    private BigDecimal monto;
    
    @Schema(description = "Denominaciones de billetes")
    private List<DetalleDenominacionDTO> denominaciones;
    
    @Schema(description = "ID del cliente", example = "CLI001")
    private String clienteId;
    
    @Schema(description = "Número de cuenta", example = "1234567890")
    private String numeroCuenta;
    
    @Schema(description = "Fecha de la transacción", example = "2024-01-01T08:00:15")
    private LocalDateTime fechaTransaccion;
    
    @Schema(description = "Observaciones", example = "Retiro en efectivo")
    private String observaciones;
} 