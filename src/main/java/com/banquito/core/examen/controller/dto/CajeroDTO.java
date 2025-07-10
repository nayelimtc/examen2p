package com.banquito.core.examen.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Información de un cajero")
public class CajeroDTO {
    
    @Schema(description = "ID del cajero", example = "507f1f77bcf86cd799439012")
    private String id;
    
    @NotBlank(message = "El código del cajero es requerido")
    @Schema(description = "Código del cajero", example = "CAJ001")
    private String codigo;
    
    @NotBlank(message = "El código de la caja es requerido")
    @Schema(description = "Código de la caja", example = "CAJA01")
    private String codigoCaja;
    
    @NotBlank(message = "El nombre es requerido")
    @Schema(description = "Nombre del cajero", example = "Juan")
    private String nombre;
    
    @NotBlank(message = "El apellido es requerido")
    @Schema(description = "Apellido del cajero", example = "Pérez")
    private String apellido;
    
    @Email(message = "El email debe ser válido")
    @Schema(description = "Email del cajero", example = "juan.perez@banquito.com")
    private String email;
    
    @NotBlank(message = "La agencia es requerida")
    @Schema(description = "Código de la agencia", example = "MATRIZ")
    private String agencia;
    
    @Schema(description = "Estado activo del cajero", example = "true")
    private Boolean activo;
    
    @Schema(description = "Fecha de creación", example = "2024-01-01T08:00:00")
    private LocalDateTime fechaCreacion;
    
    @Schema(description = "Fecha de última actualización", example = "2024-01-01T08:00:00")
    private LocalDateTime fechaActualizacion;
} 