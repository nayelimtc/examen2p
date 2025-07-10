package com.banquito.core.examen.repository;

import com.banquito.core.examen.model.TurnoCaja;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoCajaRepository extends MongoRepository<TurnoCaja, String> {
    Optional<TurnoCaja> findByCodigoTurno(String codigoTurno);
}