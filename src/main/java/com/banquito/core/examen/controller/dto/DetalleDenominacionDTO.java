package com.banquito.core.examen.controller.dto;

import com.banquito.core.examen.model.DetalleDenominacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(description = "Detalle de denominación de billetes")
public class DetalleDenominacionDTO {
    
    @NotNull(message = "La denominación es requerida")
    @Pattern(regexp = "^(1|5|10|20|50|100)$", message = "La denominación debe ser: 1, 5, 10, 20, 50 o 100")
    @Schema(description = "Denominación del billete", example = "20", allowableValues = {"1", "5", "10", "20", "50", "100"})
    private String denominacion;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Schema(description = "Cantidad de billetes", example = "5")
    private Integer cantidad;
    
    @Schema(description = "Valor total calculado", example = "100.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal valorTotal;
    
    public DetalleDenominacionDTO(String denominacion, Integer cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
        this.valorTotal = DetalleDenominacion.getValorDenominacion(denominacion)
                .multiply(new BigDecimal(cantidad));
    }
} 