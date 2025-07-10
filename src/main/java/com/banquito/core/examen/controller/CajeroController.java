package com.banquito.core.examen.controller;

import com.banquito.core.examen.controller.dto.CajeroDTO;
import com.banquito.core.examen.controller.mapper.CajeroMapper;
import com.banquito.core.examen.exception.NotFoundException;
import com.banquito.core.examen.model.Cajero;
import com.banquito.core.examen.service.CajeroService;
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
@RequestMapping("/v1/cajeros")
@Tag(name = "Cajeros", description = "Operaciones para la gestión de cajeros")
@Slf4j
public class CajeroController {

    private final CajeroService cajeroService;
    private final CajeroMapper cajeroMapper;

    public CajeroController(CajeroService cajeroService, CajeroMapper cajeroMapper) {
        this.cajeroService = cajeroService;
        this.cajeroMapper = cajeroMapper;
    }

    @PostMapping
    @Operation(summary = "Crear cajero", description = "Crea un nuevo cajero en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cajero creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<CajeroDTO> crearCajero(@Valid @RequestBody CajeroDTO cajeroDTO) {
        log.info("Solicitud para crear cajero: {}", cajeroDTO.getCodigo());
        
        Cajero cajero = cajeroService.create(cajeroMapper.toModel(cajeroDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(cajeroMapper.toDTO(cajero));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los cajeros", description = "Obtiene la lista paginada de cajeros")
    public ResponseEntity<Page<CajeroDTO>> obtenerTodosCajeros(
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info("Solicitud para obtener todos los cajeros paginados");
        Page<CajeroDTO> page = cajeroService.findAll(pageable).map(cajeroMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cajero por ID", description = "Obtiene un cajero específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cajero encontrado"),
            @ApiResponse(responseCode = "404", description = "Cajero no encontrado")
    })
    public ResponseEntity<CajeroDTO> obtenerCajeroPorId(
            @Parameter(description = "ID del cajero", example = "507f1f77bcf86cd799439012")
            @PathVariable String id) {
        log.info("Buscando cajero por ID: {}", id);
        
        try {
            Cajero cajero = cajeroService.findById(id);
            return ResponseEntity.ok(cajeroMapper.toDTO(cajero));
        } catch (NotFoundException e) {
            log.error("Cajero no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener cajero por código", description = "Obtiene un cajero específico por su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cajero encontrado"),
            @ApiResponse(responseCode = "404", description = "Cajero no encontrado")
    })
    public ResponseEntity<CajeroDTO> obtenerCajeroPorCodigo(
            @Parameter(description = "Código del cajero", example = "CAJ001")
            @PathVariable String codigo) {
        log.info("Buscando cajero por código: {}", codigo);
        
        try {
            Cajero cajero = cajeroService.findByCodigo(codigo);
            return ResponseEntity.ok(cajeroMapper.toDTO(cajero));
        } catch (NotFoundException e) {
            log.error("Cajero no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/agencia/{agencia}")
    @Operation(summary = "Obtener cajeros por agencia", description = "Obtiene cajeros filtrados por agencia con paginación")
    public ResponseEntity<Page<CajeroDTO>> obtenerCajerosPorAgencia(
            @PathVariable String agencia,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info("Buscar cajeros por agencia: {}", agencia);
        Page<CajeroDTO> page = cajeroService.findByAgencia(agencia, pageable).map(cajeroMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener cajeros activos", description = "Obtiene los cajeros activos con paginación")
    public ResponseEntity<Page<CajeroDTO>> obtenerCajerosActivos(
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info("Solicitud para obtener cajeros activos paginados");
        Page<CajeroDTO> page = cajeroService.findActivos(pageable).map(cajeroMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        log.error("Recurso no encontrado: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
} 