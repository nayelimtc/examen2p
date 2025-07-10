package com.banquito.core.examen.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Datos para procesar una transacción")
public class ProcesarTransaccionDTO {
    
    @NotBlank(message = "El ID del turno es requerido")
    @Schema(description = "ID del turno", example = "507f1f77bcf86cd799439011")
    private String turnoId;
    
    @NotNull(message = "El tipo de transacción es requerido")
    @Pattern(regexp = "^(RETIRO|DEPOSITO)$", message = "El tipo debe ser RETIRO o DEPOSITO")
    @Schema(description = "Tipo de transacción", example = "RETIRO", allowableValues = {"RETIRO", "DEPOSITO"})
    private String tipo;
    
    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Schema(description = "Monto de la transacción", example = "100.00")
    private BigDecimal monto;
    
    @NotEmpty(message = "Las denominaciones son requeridas")
    @Valid
    @Schema(description = "Denominaciones de billetes")
    private List<DetalleDenominacionDTO> denominaciones;
    
    @Schema(description = "ID del cliente", example = "CLI001")
    private String clienteId;
    
    @Schema(description = "Número de cuenta", example = "1234567890")
    private String numeroCuenta;
    
    @Schema(description = "Observaciones", example = "Retiro en efectivo")
    private String observaciones;
} 