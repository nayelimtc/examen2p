package com.banquito.core.examen.repository;

import com.banquito.core.examen.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
    
    Optional<Transaccion> findByCodigoTransaccion(String codigoTransaccion);
    
    List<Transaccion> findByTurnoId(String turnoId);
    
    List<Transaccion> findByTurnoIdOrderByFechaTransaccionDesc(String turnoId);
    
    List<Transaccion> findByCajeroId(String cajeroId);
    
    List<Transaccion> findByTurnoIdAndTipo(String turnoId, String tipo);
    
    List<Transaccion> findByFechaTransaccionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Transaccion> findByClienteId(String clienteId);

    Page<Transaccion> findByTurnoIdOrderByFechaTransaccionDesc(String turnoId, Pageable pageable);
    
    Page<Transaccion> findByCajeroId(String cajeroId, Pageable pageable);
    
    Page<Transaccion> findByTurnoIdAndTipo(String turnoId, String tipo, Pageable pageable);
    
    Page<Transaccion> findByClienteId(String clienteId, Pageable pageable);
} 