package com.banquito.core.examen.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Datos para abrir un turno")
public class AbrirTurnoDTO {
    
    @NotBlank(message = "El código del cajero es requerido")
    @Schema(description = "Código del cajero", example = "CAJ001")
    private String codigoCajero;
    
    @NotEmpty(message = "El dinero inicial es requerido")
    @Valid
    @Schema(description = "Denominaciones de dinero inicial")
    private List<DetalleDenominacionDTO> dineroInicial;
    
    @Schema(description = "Observaciones de apertura", example = "Apertura de turno normal")
    private String observaciones;
} 