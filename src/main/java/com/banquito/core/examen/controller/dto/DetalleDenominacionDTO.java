package com.banquito.management.controller.dto;

import com.banquito.management.enums.Denominacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(description = "Detalle de denominación de billetes")
public class DetalleDenominacionDTO {
    
    @NotNull(message = "La denominación es requerida")
    @Schema(description = "Denominación del billete", example = "VEINTE")
    private Denominacion denominacion;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Schema(description = "Cantidad de billetes", example = "5")
    private Integer cantidad;
    
    @Schema(description = "Valor total calculado", example = "100.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal valorTotal;
    
    public DetalleDenominacionDTO(Denominacion denominacion, Integer cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
        this.valorTotal = denominacion.getValor().multiply(new BigDecimal(cantidad));
    }
} 