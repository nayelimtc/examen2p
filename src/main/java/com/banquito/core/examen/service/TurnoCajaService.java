package com.banquito.core.examen.service;

import com.banquito.core.examen.exception.TurnoCajaNotFoundException;
import com.banquito.core.examen.exception.TurnoCajaAlreadyOpenException;
import com.banquito.core.examen.exception.InvalidTurnoStateException;
import com.banquito.core.examen.model.TurnoCaja;
import com.banquito.core.examen.model.Denominacion;
import com.banquito.core.examen.repository.TurnoCajaRepository;
import com.banquito.core.examen.repository.TransaccionTurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoCajaService {

    @Autowired
    private TurnoCajaRepository turnoCajaRepository;
    
    @Autowired
    private TransaccionTurnoRepository transaccionTurnoRepository;

    @Transactional
    public TurnoCaja abrirTurno(TurnoCaja turnoCaja) {
        // Validar que no exista un turno abierto para la misma caja y cajero
        if (turnoCajaRepository.existsByCodigoCajaAndCodigoCajeroAndEstado(
                turnoCaja.getCodigoCaja(), turnoCaja.getCodigoCajero(), "ABIERTO")) {
            throw new TurnoCajaAlreadyOpenException("Ya existe un turno abierto para la caja " + 
                turnoCaja.getCodigoCaja() + " y cajero " + turnoCaja.getCodigoCajero());
        }

        // Generar código de turno
        String codigoTurno = generarCodigoTurno(turnoCaja.getCodigoCaja(), turnoCaja.getCodigoCajero());
        turnoCaja.setCodigoTurno(codigoTurno);
        
        // Configurar datos iniciales
        turnoCaja.setEstado("ABIERTO");
        turnoCaja.setFechaInicio(LocalDateTime.now());
        turnoCaja.setCreationDate(LocalDateTime.now());
        turnoCaja.setLastModifiedDate(LocalDateTime.now());
        turnoCaja.setVersion(1L);
        
        // Validar denominaciones iniciales
        if (turnoCaja.getDenominacionesIniciales() == null || turnoCaja.getDenominacionesIniciales().isEmpty()) {
            throw new InvalidTurnoStateException("Debe registrar las denominaciones iniciales");
        }
        
        // Calcular monto inicial
        BigDecimal montoInicial = calcularMontoTotal(turnoCaja.getDenominacionesIniciales());
        turnoCaja.setMontoInicial(montoInicial);
        
        return turnoCajaRepository.save(turnoCaja);
    }

    @Transactional
    public TurnoCaja cerrarTurno(String codigoTurno, List<Denominacion> denominacionesFinales) {
        TurnoCaja turnoCaja = turnoCajaRepository.findByCodigoTurno(codigoTurno);
        if (turnoCaja == null) {
            throw new TurnoCajaNotFoundException("No se encontró el turno con código: " + codigoTurno);
        }
        
        if (!"ABIERTO".equals(turnoCaja.getEstado())) {
            throw new InvalidTurnoStateException("El turno no está abierto. Estado actual: " + turnoCaja.getEstado());
        }
        
        // Calcular monto final
        BigDecimal montoFinal = calcularMontoTotal(denominacionesFinales);
        turnoCaja.setMontoFinal(montoFinal);
        turnoCaja.setDenominacionesFinales(denominacionesFinales);
        turnoCaja.setFechaCierre(LocalDateTime.now());
        turnoCaja.setEstado("CERRADO");
        turnoCaja.setLastModifiedDate(LocalDateTime.now());
        turnoCaja.setVersion(turnoCaja.getVersion() + 1);
        
        // Validar diferencias
        BigDecimal montoCalculado = calcularMontoCalculado(codigoTurno);
        if (montoFinal.compareTo(montoCalculado) != 0) {
            // Aquí se podría generar una alerta o log
            System.out.println("ALERTA: Diferencia en cierre de turno. Esperado: " + 
                montoCalculado + ", Real: " + montoFinal);
        }
        
        return turnoCajaRepository.save(turnoCaja);
    }

    public TurnoCaja obtenerTurnoPorCodigo(String codigoTurno) {
        TurnoCaja turnoCaja = turnoCajaRepository.findByCodigoTurno(codigoTurno);
        if (turnoCaja == null) {
            throw new TurnoCajaNotFoundException("No se encontró el turno con código: " + codigoTurno);
        }
        return turnoCaja;
    }

    public List<TurnoCaja> obtenerTurnosPorCaja(String codigoCaja) {
        return turnoCajaRepository.findByCodigoCaja(codigoCaja);
    }

    public List<TurnoCaja> obtenerTurnosPorCajero(String codigoCajero) {
        return turnoCajaRepository.findByCodigoCajero(codigoCajero);
    }

    public TurnoCaja obtenerTurnoAbierto(String codigoCaja, String codigoCajero) {
        return turnoCajaRepository.findByCodigoCajaAndCodigoCajeroAndEstado(codigoCaja, codigoCajero, "ABIERTO");
    }

    private String generarCodigoTurno(String codigoCaja, String codigoCajero) {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return codigoCaja + "-" + codigoCajero + "-" + fecha;
    }

    private BigDecimal calcularMontoTotal(List<Denominacion> denominaciones) {
        return denominaciones.stream()
                .map(denominacion -> denominacion.getMonto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularMontoCalculado(String codigoTurno) {
        // Obtener todas las transacciones del turno
        List<com.banquito.core.examen.model.TransaccionTurno> transacciones = 
            transaccionTurnoRepository.findByCodigoTurnoAndEstado(codigoTurno, "ACTIVO");
        
        BigDecimal montoCalculado = BigDecimal.ZERO;
        for (com.banquito.core.examen.model.TransaccionTurno transaccion : transacciones) {
            if ("DEPOSITO".equals(transaccion.getTipoTransaccion())) {
                montoCalculado = montoCalculado.add(transaccion.getMontoTotal());
            } else if ("AHORRO".equals(transaccion.getTipoTransaccion())) {
                montoCalculado = montoCalculado.subtract(transaccion.getMontoTotal());
            }
        }
        
        return montoCalculado;
    }
} 