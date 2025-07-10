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
@Schema(description = "Datos para cerrar un turno")
public class CerrarTurnoDTO {
    
    @NotBlank(message = "El código del turno es requerido")
    @Schema(description = "Código del turno", example = "CAJ001-20240101-0800")
    private String codigoTurno;
    
    @NotEmpty(message = "El dinero final es requerido")
    @Valid
    @Schema(description = "Denominaciones de dinero final")
    private List<DetalleDenominacionDTO> dineroFinal;
    
    @Schema(description = "Observaciones de cierre", example = "Cierre de turno normal")
    private String observaciones;
} 