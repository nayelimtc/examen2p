package com.banquito.core.examen.controller;

import com.banquito.core.examen.controller.dto.AbrirTurnoDTO;
import com.banquito.core.examen.controller.dto.CerrarTurnoDTO;
import com.banquito.core.examen.controller.dto.TurnoDTO;
import com.banquito.core.examen.controller.mapper.DetalleDenominacionMapper;
import com.banquito.core.examen.controller.mapper.TurnoMapper;
import com.banquito.core.examen.exception.NotFoundException;
import com.banquito.core.examen.exception.TurnoException;
import com.banquito.core.examen.model.Turno;
import com.banquito.core.examen.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/turnos")
@Tag(name = "Turnos", description = "Operaciones para el manejo de turnos de cajeros")
@Slf4j
public class TurnoController {

    private final TurnoService turnoService;
    private final TurnoMapper turnoMapper;
    private final DetalleDenominacionMapper detalleDenominacionMapper;

    public TurnoController(TurnoService turnoService, TurnoMapper turnoMapper, DetalleDenominacionMapper detalleDenominacionMapper) {
        this.turnoService = turnoService;
        this.turnoMapper = turnoMapper;
        this.detalleDenominacionMapper = detalleDenominacionMapper;
    }

    @PostMapping("/abrir")
    @Operation(summary = "Abrir turno", description = "Abre un nuevo turno para un cajero con el dinero inicial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno abierto exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cajero no encontrado"),
            @ApiResponse(responseCode = "409", description = "El cajero ya tiene un turno abierto")
    })
    public ResponseEntity<TurnoDTO> abrirTurno(@Valid @RequestBody AbrirTurnoDTO abrirTurnoDTO) {
        log.info("Solicitud para abrir turno para cajero: {}", abrirTurnoDTO.getCodigoCajero());
        
        try {
            Turno turno = turnoService.abrirTurno(
                    abrirTurnoDTO.getCodigoCajero(),
                    detalleDenominacionMapper.toModelList(abrirTurnoDTO.getDineroInicial()),
                    abrirTurnoDTO.getObservaciones()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(turnoMapper.toDTO(turno));
        } catch (NotFoundException e) {
            log.error("Cajero no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (TurnoException e) {
            log.error("Error al abrir turno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/cerrar")
    @Operation(summary = "Cerrar turno", description = "Cierra un turno existente con el dinero final y calcula diferencias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno cerrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El turno ya está cerrado")
    })
    public ResponseEntity<TurnoDTO> cerrarTurno(@Valid @RequestBody CerrarTurnoDTO cerrarTurnoDTO) {
        log.info("Solicitud para cerrar turno: {}", cerrarTurnoDTO.getCodigoTurno());
        
        try {
            Turno turno = turnoService.cerrarTurno(
                    cerrarTurnoDTO.getCodigoTurno(),
                    detalleDenominacionMapper.toModelList(cerrarTurnoDTO.getDineroFinal()),
                    cerrarTurnoDTO.getObservaciones()
            );
            
            return ResponseEntity.ok(turnoMapper.toDTO(turno));
        } catch (NotFoundException e) {
            log.error("Turno no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (TurnoException e) {
            log.error("Error al cerrar turno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{codigoTurno}")
    @Operation(summary = "Obtener turno por código", description = "Obtiene la información de un turno específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<TurnoDTO> obtenerTurnoPorCodigo(
            @Parameter(description = "Código del turno", example = "CAJ001-20240101-0800")
            @PathVariable String codigoTurno) {
        log.info("Buscando turno por código: {}", codigoTurno);
        
        try {
            Turno turno = turnoService.findByCodigoTurno(codigoTurno);
            return ResponseEntity.ok(turnoMapper.toDTO(turno));
        } catch (NotFoundException e) {
            log.error("Turno no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/agencia/{agencia}")
    @Operation(summary = "Obtener turnos abiertos por agencia", description = "Obtiene todos los turnos abiertos de una agencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turnos encontrados")
    })
    public ResponseEntity<List<TurnoDTO>> obtenerTurnosAbiertos(
            @Parameter(description = "Código de la agencia", example = "MATRIZ")
            @PathVariable String agencia) {
        log.info("Buscando turnos abiertos para agencia: {}", agencia);
        
        List<Turno> turnos = turnoService.findByAgencia(agencia);
        return ResponseEntity.ok(turnoMapper.toDTOList(turnos));
    }

    @GetMapping("/alertas")
    @Operation(summary = "Obtener turnos con alertas", description = "Obtiene todos los turnos que tienen diferencias de dinero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turnos con alertas encontrados")
    })
    public ResponseEntity<List<TurnoDTO>> obtenerTurnosConAlerta() {
        log.info("Buscando turnos con alertas");
        
        List<Turno> turnos = turnoService.findTurnosConAlerta();
        return ResponseEntity.ok(turnoMapper.toDTOList(turnos));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        log.error("Recurso no encontrado: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({TurnoException.class})
    public ResponseEntity<Void> handleTurnoException(TurnoException e) {
        log.error("Error de negocio en turno: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
} 