package com.banquito.core.examen.service;

import com.banquito.core.examen.exception.NotFoundException;
import com.banquito.core.examen.exception.TurnoException;
import com.banquito.core.examen.model.Cajero;
import com.banquito.core.examen.model.DetalleDenominacion;
import com.banquito.core.examen.model.Transaccion;
import com.banquito.core.examen.model.Turno;
import com.banquito.core.examen.repository.TurnoRepository;
import com.banquito.core.examen.repository.TransaccionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TurnoService {
    
    private final TurnoRepository turnoRepository;
    private final CajeroService cajeroService;
    private final TransaccionRepository transaccionRepository;
    
    public TurnoService(TurnoRepository turnoRepository, CajeroService cajeroService, TransaccionRepository transaccionRepository) {
        this.turnoRepository = turnoRepository;
        this.cajeroService = cajeroService;
        this.transaccionRepository = transaccionRepository;
    }
    
    public Turno abrirTurno(String codigoCajero, List<DetalleDenominacion> dineroInicial, String observaciones) {
        log.info("Iniciando apertura de turno para cajero: {}", codigoCajero);
        
        // Validar que el cajero existe y está activo
        Cajero cajero = cajeroService.findByCodigoActivo(codigoCajero);
        
        // Validar que el cajero no tenga un turno abierto
        Optional<Turno> turnoAbierto = turnoRepository.findByCajeroIdAndEstado(cajero.getId(), Turno.ESTADO_ABIERTO);
        if (turnoAbierto.isPresent()) {
            throw new TurnoException("Apertura de turno", "El cajero ya tiene un turno abierto");
        }
        
        // Validar que tenga dinero inicial
        if (dineroInicial == null || dineroInicial.isEmpty()) {
            throw new TurnoException("Apertura de turno", "Debe especificar el dinero inicial");
        }
        
        // Crear el turno
        Turno turno = new Turno();
        turno.setCodigoTurno(generarCodigoTurno(cajero.getCodigoCaja(), cajero.getCodigo()));
        turno.setCajeroId(cajero.getId());
        turno.setCodigoCajero(cajero.getCodigo());
        turno.setCodigoCaja(cajero.getCodigoCaja());
        turno.setAgencia(cajero.getAgencia());
        turno.setEstado(Turno.ESTADO_ABIERTO);
        turno.setFechaApertura(LocalDateTime.now());
        turno.setDineroInicial(dineroInicial);
        turno.setMontoCalculado(turno.calcularMontoInicial());
        turno.setTieneAlerta(false);
        turno.setObservaciones(observaciones);
        
        Turno turnoGuardado = turnoRepository.save(turno);
        log.info("Turno abierto exitosamente: {}", turnoGuardado.getCodigoTurno());
        
        return turnoGuardado;
    }
    
    public Turno cerrarTurno(String codigoTurno, List<DetalleDenominacion> dineroFinal, String observaciones) {
        log.info("Iniciando cierre de turno: {}", codigoTurno);
        
        // Buscar el turno
        Turno turno = findByCodigoTurno(codigoTurno);
        
        // Validar que el turno esté abierto
        if (!Turno.ESTADO_ABIERTO.equals(turno.getEstado())) {
            throw new TurnoException("Cierre de turno", "El turno no está abierto");
        }
        
        // Validar que tenga dinero final
        if (dineroFinal == null || dineroFinal.isEmpty()) {
            throw new TurnoException("Cierre de turno", "Debe especificar el dinero final");
        }
        
        // Calcular monto esperado basado en transacciones
        BigDecimal montoEsperado = calcularMontoEsperado(turno.getId());
        
        // Calcular monto declarado
        BigDecimal montoDeclarado = dineroFinal.stream()
                .map(DetalleDenominacion::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calcular diferencia
        BigDecimal diferencia = montoDeclarado.subtract(montoEsperado);
        
        // Actualizar turno
        turno.setEstado(Turno.ESTADO_CERRADO);
        turno.setFechaCierre(LocalDateTime.now());
        turno.setDineroFinal(dineroFinal);
        turno.setMontoCalculado(montoEsperado);
        turno.setMontoDeclarado(montoDeclarado);
        turno.setDiferencia(diferencia);
        turno.setTieneAlerta(diferencia.compareTo(BigDecimal.ZERO) != 0);
        
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            String observacionesActuales = turno.getObservaciones() != null ? turno.getObservaciones() : "";
            turno.setObservaciones(observacionesActuales + " | Cierre: " + observaciones);
        }
        
        Turno turnoCerrado = turnoRepository.save(turno);
        
        if (turnoCerrado.getTieneAlerta()) {
            log.warn("Turno cerrado con diferencia: {} - Diferencia: {}", 
                    turnoCerrado.getCodigoTurno(), diferencia);
        } else {
            log.info("Turno cerrado exitosamente: {}", turnoCerrado.getCodigoTurno());
        }
        
        return turnoCerrado;
    }
    
    public Turno findById(String id) {
        log.info("Buscando turno por ID: {}", id);
        Optional<Turno> turno = turnoRepository.findById(id);
        if (turno.isPresent()) {
            return turno.get();
        }
        throw new NotFoundException(id, "Turno");
    }
    
    public Turno findByCodigoTurno(String codigoTurno) {
        log.info("Buscando turno por código: {}", codigoTurno);
        Optional<Turno> turno = turnoRepository.findByCodigoTurno(codigoTurno);
        if (turno.isPresent()) {
            return turno.get();
        }
        throw new NotFoundException(codigoTurno, "Turno");
    }
    
    public List<Turno> findByAgencia(String agencia) {
        log.info("Buscando turnos por agencia: {}", agencia);
        return turnoRepository.findByAgenciaAndEstado(agencia, Turno.ESTADO_ABIERTO);
    }
    
    public Page<Turno> findByAgencia(String agencia, Pageable pageable) {
        log.info("Buscando turnos por agencia '{}' paginados: {}", agencia, pageable);
        return turnoRepository.findByAgenciaAndEstado(agencia, Turno.ESTADO_ABIERTO, pageable);
    }
    
    public List<Turno> findTurnosConAlerta() {
        log.info("Buscando turnos con alerta");
        return turnoRepository.findByTieneAlertaTrue();
    }
    
    public Page<Turno> findTurnosConAlerta(Pageable pageable) {
        log.info("Buscando turnos con alerta paginados");
        return turnoRepository.findByTieneAlertaTrue(pageable);
    }
    
    public Optional<Turno> findTurnoAbiertoPorCajero(String cajeroId) {
        log.info("Buscando turno abierto para cajero: {}", cajeroId);
        return turnoRepository.findByCajeroIdAndEstado(cajeroId, Turno.ESTADO_ABIERTO);
    }
    
    private String generarCodigoTurno(String codigoCaja, String codigoCajero) {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("%s-%s-%s", codigoCaja, codigoCajero, fecha);
    }
    
    private BigDecimal calcularMontoEsperado(String turnoId) {
        log.info("Calculando monto esperado para turno: {}", turnoId);
        
        // Obtener el turno para conocer el monto inicial
        Turno turno = findById(turnoId);
        BigDecimal montoInicial = turno.calcularMontoInicial();
        
        // Obtener todas las transacciones del turno
        List<Transaccion> transacciones = transaccionRepository.findByTurnoIdOrderByFechaTransaccionDesc(turnoId);
        
        // Calcular el impacto de las transacciones
        BigDecimal impactoTransacciones = transacciones.stream()
                .map(transaccion -> {
                    BigDecimal monto = transaccion.calcularMontoTotal();
                    // Los depósitos suman al dinero en caja, los retiros restan
                    return switch (transaccion.getTipo()) {
                        case Transaccion.TIPO_DEPOSITO -> monto;
                        case Transaccion.TIPO_RETIRO -> monto.negate();
                        default -> BigDecimal.ZERO;
                    };
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return montoInicial.add(impactoTransacciones);
    }
} 