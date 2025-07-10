package com.banquito.core.examen.service;

import com.banquito.core.examen.exception.NotFoundException;
import com.banquito.core.examen.exception.TransaccionException;
import com.banquito.core.examen.model.DetalleDenominacion;
import com.banquito.core.examen.model.Transaccion;
import com.banquito.core.examen.model.Turno;
import com.banquito.core.examen.repository.TransaccionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransaccionService {
    
    private final TransaccionRepository transaccionRepository;
    private final TurnoService turnoService;
    
    public TransaccionService(TransaccionRepository transaccionRepository, @Lazy TurnoService turnoService) {
        this.transaccionRepository = transaccionRepository;
        this.turnoService = turnoService;
    }
    
    public Transaccion procesarTransaccion(String turnoId, String tipo, BigDecimal monto, 
                                          List<DetalleDenominacion> denominaciones, String clienteId, 
                                          String numeroCuenta, String observaciones) {
        log.info("Procesando transacción tipo: {} para turno: {}", tipo, turnoId);
        
        // Validar que el turno existe y está abierto
        Turno turno = turnoService.findById(turnoId);
        if (!Turno.ESTADO_ABIERTO.equals(turno.getEstado())) {
            throw new TransaccionException("Procesar transacción", "El turno no está abierto");
        }
        
        // Validar tipo de transacción
        if (!Transaccion.esValidoTipo(tipo)) {
            throw new TransaccionException("Procesar transacción", "Tipo de transacción no válido");
        }
        
        // Validar monto
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransaccionException("Procesar transacción", "El monto debe ser mayor a cero");
        }
        
        // Validar denominaciones
        if (denominaciones == null || denominaciones.isEmpty()) {
            throw new TransaccionException("Procesar transacción", "Debe especificar las denominaciones");
        }
        
        // Validar que el monto coincida con las denominaciones
        BigDecimal montoCalculado = denominaciones.stream()
                .map(DetalleDenominacion::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (monto.compareTo(montoCalculado) != 0) {
            throw new TransaccionException("Procesar transacción", 
                    "El monto no coincide con las denominaciones especificadas");
        }
        
        // Crear la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setCodigoTransaccion(generarCodigoTransaccion(turno.getCodigoCajero()));
        transaccion.setTurnoId(turnoId);
        transaccion.setCajeroId(turno.getCajeroId());
        transaccion.setCodigoCajero(turno.getCodigoCajero());
        transaccion.setCodigoCaja(turno.getCodigoCaja());
        transaccion.setTipo(tipo);
        transaccion.setMonto(monto);
        transaccion.setDenominaciones(denominaciones);
        transaccion.setClienteId(clienteId);
        transaccion.setNumeroCuenta(numeroCuenta);
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setObservaciones(observaciones);
        
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
        log.info("Transacción procesada exitosamente: {}", transaccionGuardada.getCodigoTransaccion());
        
        return transaccionGuardada;
    }
    
    public Transaccion findById(String id) {
        log.info("Buscando transacción por ID: {}", id);
        Optional<Transaccion> transaccion = transaccionRepository.findById(id);
        if (transaccion.isPresent()) {
            return transaccion.get();
        }
        throw new NotFoundException(id, "Transacción");
    }
    
    public Transaccion findByCodigoTransaccion(String codigoTransaccion) {
        log.info("Buscando transacción por código: {}", codigoTransaccion);
        Optional<Transaccion> transaccion = transaccionRepository.findByCodigoTransaccion(codigoTransaccion);
        if (transaccion.isPresent()) {
            return transaccion.get();
        }
        throw new NotFoundException(codigoTransaccion, "Transacción");
    }
    
    public List<Transaccion> findByTurnoId(String turnoId) {
        log.info("Buscando transacciones por turno ID: {}", turnoId);
        return transaccionRepository.findByTurnoIdOrderByFechaTransaccionDesc(turnoId);
    }
    
    public Page<Transaccion> findByTurnoId(String turnoId, Pageable pageable) {
        log.info("Buscando transacciones por turno paginadas: {}", turnoId);
        return transaccionRepository.findByTurnoIdOrderByFechaTransaccionDesc(turnoId, pageable);
    }
    
    public List<Transaccion> findByCajeroId(String cajeroId) {
        log.info("Buscando transacciones por cajero ID: {}", cajeroId);
        return transaccionRepository.findByCajeroId(cajeroId);
    }
    
    public Page<Transaccion> findByCajeroId(String cajeroId, Pageable pageable) {
        log.info("Buscando transacciones por cajero paginadas: {}", cajeroId);
        return transaccionRepository.findByCajeroId(cajeroId, pageable);
    }
    
    public List<Transaccion> findByClienteId(String clienteId) {
        log.info("Buscando transacciones por cliente ID: {}", clienteId);
        return transaccionRepository.findByClienteId(clienteId);
    }
    
    public Page<Transaccion> findByClienteId(String clienteId, Pageable pageable) {
        log.info("Buscando transacciones por cliente paginadas: {}", clienteId);
        return transaccionRepository.findByClienteId(clienteId, pageable);
    }
    
    public List<Transaccion> findByTurnoIdAndTipo(String turnoId, String tipo) {
        log.info("Buscando transacciones por turno ID: {} y tipo: {}", turnoId, tipo);
        return transaccionRepository.findByTurnoIdAndTipo(turnoId, tipo);
    }
    
    public Page<Transaccion> findByTurnoIdAndTipo(String turnoId, String tipo, Pageable pageable) {
        log.info("Buscando transacciones por turno y tipo paginadas");
        return transaccionRepository.findByTurnoIdAndTipo(turnoId, tipo, pageable);
    }
    
    private String generarCodigoTransaccion(String codigoCajero) {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hora = ahora.format(DateTimeFormatter.ofPattern("HHmmss"));
        return String.format("TXN-%s-%s-%s", codigoCajero, fecha, hora);
    }
} 

