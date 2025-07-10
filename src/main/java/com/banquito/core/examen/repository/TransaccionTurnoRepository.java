package com.banquito.core.examen.repository;

import com.banquito.core.examen.model.TransaccionTurno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransaccionTurnoRepository extends MongoRepository<TransaccionTurno, String> {
    
    // Buscar transacciones por código de turno
    List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
    
    // Buscar transacciones por código de turno y tipo
    List<TransaccionTurno> findByCodigoTurnoAndTipoTransaccion(String codigoTurno, String tipoTransaccion);
    
    // Buscar transacciones por código de turno y estado
    List<TransaccionTurno> findByCodigoTurnoAndEstado(String codigoTurno, String estado);
    
    // Buscar transacciones por caja y cajero
    List<TransaccionTurno> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    // Buscar transacciones por caja, cajero y fecha
    List<TransaccionTurno> findByCodigoCajaAndCodigoCajeroAndFechaTransaccionBetween(
        String codigoCaja, String codigoCajero, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar transacciones por tipo y estado
    List<TransaccionTurno> findByTipoTransaccionAndEstado(String tipoTransaccion, String estado);
    
    // Contar transacciones por turno
    long countByCodigoTurno(String codigoTurno);
} 