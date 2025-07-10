package com.banquito.core.examen.controller;

import com.banquito.core.examen.controller.dto.ProcesarTransaccionDTO;
import com.banquito.core.examen.controller.dto.TransaccionDTO;
import com.banquito.core.examen.controller.mapper.DetalleDenominacionMapper;
import com.banquito.core.examen.controller.mapper.TransaccionMapper;
import com.banquito.core.examen.exception.NotFoundException;
import com.banquito.core.examen.exception.TransaccionException;
import com.banquito.core.examen.model.Transaccion;
import com.banquito.core.examen.service.TransaccionService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/v1/transacciones")
@Tag(name = "Transacciones", description = "Operaciones para el procesamiento de transacciones")
@Slf4j
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final TransaccionMapper transaccionMapper;
    private final DetalleDenominacionMapper detalleDenominacionMapper;

    public TransaccionController(TransaccionService transaccionService, TransaccionMapper transaccionMapper, DetalleDenominacionMapper detalleDenominacionMapper) {
        this.transaccionService = transaccionService;
        this.transaccionMapper = transaccionMapper;
        this.detalleDenominacionMapper = detalleDenominacionMapper;
    }

    @PostMapping
    @Operation(summary = "Procesar transacción", description = "Procesa una nueva transacción de retiro o depósito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción procesada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @ApiResponse(responseCode = "409", description = "Error en el procesamiento de la transacción")
    })
    public ResponseEntity<TransaccionDTO> procesarTransaccion(@Valid @RequestBody ProcesarTransaccionDTO procesarTransaccionDTO) {
        log.info("Solicitud para procesar transacción tipo: {} para turno: {}", 
                procesarTransaccionDTO.getTipo(), procesarTransaccionDTO.getTurnoId());
        
        try {
            Transaccion transaccion = transaccionService.procesarTransaccion(
                    procesarTransaccionDTO.getTurnoId(),
                    procesarTransaccionDTO.getTipo(),
                    procesarTransaccionDTO.getMonto(),
                    detalleDenominacionMapper.toModelList(procesarTransaccionDTO.getDenominaciones()),
                    procesarTransaccionDTO.getClienteId(),
                    procesarTransaccionDTO.getNumeroCuenta(),
                    procesarTransaccionDTO.getObservaciones()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccionMapper.toDTO(transaccion));
        } catch (NotFoundException e) {
            log.error("Turno no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (TransaccionException e) {
            log.error("Error al procesar transacción: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{codigoTransaccion}")
    @Operation(summary = "Obtener transacción por código", description = "Obtiene la información de una transacción específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción encontrada"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public ResponseEntity<TransaccionDTO> obtenerTransaccionPorCodigo(
            @Parameter(description = "Código de la transacción", example = "TXN-CAJ001-20240101-080015")
            @PathVariable String codigoTransaccion) {
        log.info("Buscando transacción por código: {}", codigoTransaccion);
        
        try {
            Transaccion transaccion = transaccionService.findByCodigoTransaccion(codigoTransaccion);
            return ResponseEntity.ok(transaccionMapper.toDTO(transaccion));
        } catch (NotFoundException e) {
            log.error("Transacción no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/turno/{turnoId}")
    @Operation(summary = "Obtener transacciones por turno", description = "Obtiene todas las transacciones de un turno específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones encontradas")
    })
    public ResponseEntity<Page<TransaccionDTO>> obtenerTransaccionesPorTurno(
            @Parameter(description = "ID del turno", example = "507f1f77bcf86cd799439011")
            @PathVariable String turnoId,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info("Buscando transacciones para turno: {}", turnoId);
        Page<TransaccionDTO> page = transaccionService.findByTurnoId(turnoId, pageable).map(transaccionMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/cajero/{cajeroId}")
    @Operation(summary = "Obtener transacciones por cajero", description = "Obtiene todas las transacciones de un cajero específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones encontradas")
    })
    public ResponseEntity<Page<TransaccionDTO>> obtenerTransaccionesPorCajero(
            @Parameter(description = "ID del cajero", example = "507f1f77bcf86cd799439012")
            @PathVariable String cajeroId,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info("Buscando transacciones para cajero: {}", cajeroId);
        Page<TransaccionDTO> page = transaccionService.findByCajeroId(cajeroId, pageable).map(transaccionMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener transacciones por cliente", description = "Obtiene todas las transacciones de un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones encontradas")
    })
    public ResponseEntity<Page<TransaccionDTO>> obtenerTransaccionesPorCliente(
            @Parameter(description = "ID del cliente", example = "CLI001")
            @PathVariable String clienteId,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info("Buscando transacciones para cliente: {}", clienteId);
        Page<TransaccionDTO> page = transaccionService.findByClienteId(clienteId, pageable).map(transaccionMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        log.error("Recurso no encontrado: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({TransaccionException.class})
    public ResponseEntity<Void> handleTransaccionException(TransaccionException e) {
        log.error("Error de negocio en transacción: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
} 