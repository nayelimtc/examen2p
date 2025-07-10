package com.banquito.core.examen.repository;

import com.banquito.core.examen.model.Cajero;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CajeroRepository extends MongoRepository<Cajero, String> {
    
    Optional<Cajero> findByCodigo(String codigo);
    
    List<Cajero> findByAgencia(String agencia);
    
    List<Cajero> findByActivoTrue();
    
    Optional<Cajero> findByCodigoAndActivoTrue(String codigo);

    org.springframework.data.domain.Page<Cajero> findAll(org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Cajero> findByAgencia(String agencia, org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Cajero> findByActivoTrue(org.springframework.data.domain.Pageable pageable);
} 