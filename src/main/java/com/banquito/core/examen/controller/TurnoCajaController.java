package com.banquito.core.examen.controller;

import com.banquito.core.examen.model.TurnoCaja;
import com.banquito.core.examen.model.Denominacion;
import com.banquito.core.examen.service.TurnoCajaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
@Tag(name = "Turnos de Caja", description = "API para el manejo de turnos de caja")
public class TurnoCajaController {

    private static final Logger logger = LoggerFactory.getLogger(TurnoCajaController.class);

    @Autowired
    private TurnoCajaService turnoCajaService;

    @PostMapping("/abrir")
    @Operation(
        summary = "Abrir turno de caja",
        description = "Permite abrir un nuevo turno de caja con las denominaciones iniciales"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Turno abierto exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoCaja.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un turno abierto para esta caja y cajero")
    })
    public ResponseEntity<TurnoCaja> abrirTurno(
            @Parameter(description = "Datos del turno a abrir", required = true)
            @RequestBody TurnoCaja turnoCaja) {
        
        logger.info("Iniciando apertura de turno para caja: {} y cajero: {}", 
            turnoCaja.getCodigoCaja(), turnoCaja.getCodigoCajero());
        
        try {
            TurnoCaja turnoAbierto = turnoCajaService.abrirTurno(turnoCaja);
            logger.info("Turno abierto exitosamente con código: {}", turnoAbierto.getCodigoTurno());
            return ResponseEntity.status(HttpStatus.CREATED).body(turnoAbierto);
        } catch (Exception e) {
            logger.error("Error al abrir turno: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/{codigoTurno}/cerrar")
    @Operation(
        summary = "Cerrar turno de caja",
        description = "Permite cerrar un turno de caja con las denominaciones finales"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno cerrado exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoCaja.class))),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido del turno")
    })
    public ResponseEntity<TurnoCaja> cerrarTurno(
            @Parameter(description = "Código del turno a cerrar", required = true)
            @PathVariable String codigoTurno,
            @Parameter(description = "Denominaciones finales del turno", required = true)
            @RequestBody List<Denominacion> denominacionesFinales) {
        
        logger.info("Iniciando cierre de turno: {}", codigoTurno);
        
        try {
            TurnoCaja turnoCerrado = turnoCajaService.cerrarTurno(codigoTurno, denominacionesFinales);
            logger.info("Turno cerrado exitosamente: {}", codigoTurno);
            return ResponseEntity.ok(turnoCerrado);
        } catch (Exception e) {
            logger.error("Error al cerrar turno {}: {}", codigoTurno, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{codigoTurno}")
    @Operation(
        summary = "Obtener turno por código",
        description = "Obtiene la información de un turno específico por su código"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno encontrado",
            content = @Content(schema = @Schema(implementation = TurnoCaja.class))),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<TurnoCaja> obtenerTurno(
            @Parameter(description = "Código del turno", required = true)
            @PathVariable String codigoTurno) {
        
        logger.info("Consultando turno: {}", codigoTurno);
        
        try {
            TurnoCaja turno = turnoCajaService.obtenerTurnoPorCodigo(codigoTurno);
            return ResponseEntity.ok(turno);
        } catch (Exception e) {
            logger.error("Error al consultar turno {}: {}", codigoTurno, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/caja/{codigoCaja}")
    @Operation(
        summary = "Obtener turnos por caja",
        description = "Obtiene todos los turnos de una caja específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de turnos encontrados",
            content = @Content(schema = @Schema(implementation = TurnoCaja.class)))
    })
    public ResponseEntity<List<TurnoCaja>> obtenerTurnosPorCaja(
            @Parameter(description = "Código de la caja", required = true)
            @PathVariable String codigoCaja) {
        
        logger.info("Consultando turnos para caja: {}", codigoCaja);
        
        List<TurnoCaja> turnos = turnoCajaService.obtenerTurnosPorCaja(codigoCaja);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/cajero/{codigoCajero}")
    @Operation(
        summary = "Obtener turnos por cajero",
        description = "Obtiene todos los turnos de un cajero específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de turnos encontrados",
            content = @Content(schema = @Schema(implementation = TurnoCaja.class)))
    })
    public ResponseEntity<List<TurnoCaja>> obtenerTurnosPorCajero(
            @Parameter(description = "Código del cajero", required = true)
            @PathVariable String codigoCajero) {
        
        logger.info("Consultando turnos para cajero: {}", codigoCajero);
        
        List<TurnoCaja> turnos = turnoCajaService.obtenerTurnosPorCajero(codigoCajero);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/abierto")
    @Operation(
        summary = "Obtener turno abierto",
        description = "Obtiene el turno abierto para una caja y cajero específicos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno abierto encontrado",
            content = @Content(schema = @Schema(implementation = TurnoCaja.class))),
        @ApiResponse(responseCode = "404", description = "No hay turno abierto")
    })
    public ResponseEntity<TurnoCaja> obtenerTurnoAbierto(
            @Parameter(description = "Código de la caja", required = true)
            @RequestParam String codigoCaja,
            @Parameter(description = "Código del cajero", required = true)
            @RequestParam String codigoCajero) {
        
        logger.info("Consultando turno abierto para caja: {} y cajero: {}", codigoCaja, codigoCajero);
        
        try {
            TurnoCaja turno = turnoCajaService.obtenerTurnoAbierto(codigoCaja, codigoCajero);
            return ResponseEntity.ok(turno);
        } catch (Exception e) {
            logger.error("Error al consultar turno abierto: {}", e.getMessage());
            throw e;
        }
    }
} 