package com.banquito.core.examen.repository;

import com.banquito.core.examen.model.Turno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TurnoRepository extends MongoRepository<Turno, String> {
    
    Optional<Turno> findByCodigoTurno(String codigoTurno);
    
    Optional<Turno> findByCajeroIdAndEstado(String cajeroId, String estado);
    
    List<Turno> findByCajeroIdAndEstadoOrderByFechaAperturaDesc(String cajeroId, String estado);
    
    List<Turno> findByCajeroId(String cajeroId);
    
    List<Turno> findByAgenciaAndEstado(String agencia, String estado);
    
    List<Turno> findByFechaAperturaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Turno> findByTieneAlertaTrue();

    Page<Turno> findByAgenciaAndEstado(String agencia, String estado, Pageable pageable);
    
    Page<Turno> findByTieneAlertaTrue(Pageable pageable);
} 