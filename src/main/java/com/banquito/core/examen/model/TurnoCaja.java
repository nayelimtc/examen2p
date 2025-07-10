package com.banquito.core.examen.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Document(collection = "turno_cajas")
public class TurnoCaja {
    @Id
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;          
    private BigDecimal montoInicial;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private BigDecimal montoFinal;
   
}
