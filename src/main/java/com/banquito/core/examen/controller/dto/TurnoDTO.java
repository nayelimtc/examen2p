package com.banquito.core.examen.controller.dto;

import com.banquito.core.examen.enums.EstadoTurno;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Información de un turno")
public class TurnoDTO {
    
    @Schema(description = "ID del turno", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Código del turno", example = "CAJ001-20240101-0800")
    private String codigoTurno;
    
    @Schema(description = "ID del cajero", example = "507f1f77bcf86cd799439012")
    private String cajeroId;
    
    @Schema(description = "Código del cajero", example = "CAJ001")
    private String codigoCajero;
    
    @Schema(description = "Agencia", example = "MATRIZ")
    private String agencia;
    
    @Schema(description = "Estado del turno", example = "ABIERTO")
    private EstadoTurno estado;
    
    @Schema(description = "Fecha de apertura", example = "2024-01-01T08:00:00")
    private LocalDateTime fechaApertura;
    
    @Schema(description = "Fecha de cierre", example = "2024-01-01T17:00:00")
    private LocalDateTime fechaCierre;
    
    @Schema(description = "Denominaciones de dinero inicial")
    private List<DetalleDenominacionDTO> dineroInicial;
    
    @Schema(description = "Denominaciones de dinero final")
    private List<DetalleDenominacionDTO> dineroFinal;
    
    @Schema(description = "Monto calculado basado en transacciones", example = "1000.00")
    private BigDecimal montoCalculado;
    
    @Schema(description = "Monto declarado por el cajero", example = "1000.00")
    private BigDecimal montoDeclarado;
    
    @Schema(description = "Diferencia entre monto declarado y calculado", example = "0.00")
    private BigDecimal diferencia;
    
    @Schema(description = "Indica si hay una alerta por diferencia", example = "false")
    private Boolean tieneAlerta;
    
    @Schema(description = "Observaciones del turno", example = "Turno normal sin novedades")
    private String observaciones;
} 