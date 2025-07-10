package com.banquito.core.examen.repository;

import com.banquito.core.examen.model.TurnoCaja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TurnoCajaRepository extends MongoRepository<TurnoCaja, String> {
    
    // Buscar turno por código de turno
    TurnoCaja findByCodigoTurno(String codigoTurno);
    
    // Buscar turnos por código de caja
    List<TurnoCaja> findByCodigoCaja(String codigoCaja);
    
    // Buscar turnos por código de cajero
    List<TurnoCaja> findByCodigoCajero(String codigoCajero);
    
    // Buscar turnos por estado
    List<TurnoCaja> findByEstado(String estado);
    
    // Buscar turno abierto por caja y cajero
    TurnoCaja findByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, String estado);
    
    // Verificar si existe un turno abierto para una caja y cajero específicos
    boolean existsByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, String estado);
    
    // Buscar turnos por caja y estado
    List<TurnoCaja> findByCodigoCajaAndEstado(String codigoCaja, String estado);
}
