package com.banquito.core.examen.service;

import com.banquito.core.examen.exception.NotFoundException;
import com.banquito.core.examen.model.Cajero;
import com.banquito.core.examen.repository.CajeroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
public class CajeroService {
    
    private final CajeroRepository cajeroRepository;
    
    public CajeroService(CajeroRepository cajeroRepository) {
        this.cajeroRepository = cajeroRepository;
    }
    
    public List<Cajero> findAll() {
        log.info("Buscando todos los cajeros");
        return cajeroRepository.findAll();
    }
    
    public Cajero findById(String id) {
        log.info("Buscando cajero por ID: {}", id);
        Optional<Cajero> cajero = cajeroRepository.findById(id);
        if (cajero.isPresent()) {
            return cajero.get();
        }
        throw new NotFoundException(id, "Cajero");
    }
    
    public Cajero findByCodigo(String codigo) {
        log.info("Buscando cajero por código: {}", codigo);
        Optional<Cajero> cajero = cajeroRepository.findByCodigo(codigo);
        if (cajero.isPresent()) {
            return cajero.get();
        }
        throw new NotFoundException(codigo, "Cajero");
    }
    
    public Cajero findByCodigoActivo(String codigo) {
        log.info("Buscando cajero activo por código: {}", codigo);
        Optional<Cajero> cajero = cajeroRepository.findByCodigoAndActivoTrue(codigo);
        if (cajero.isPresent()) {
            return cajero.get();
        }
        throw new NotFoundException(codigo, "Cajero activo");
    }
    
    public List<Cajero> findByAgencia(String agencia) {
        log.info("Buscando cajeros por agencia: {}", agencia);
        return cajeroRepository.findByAgencia(agencia);
    }
    
    public List<Cajero> findActivos() {
        log.info("Buscando cajeros activos");
        return cajeroRepository.findByActivoTrue();
    }
    
    public Cajero create(Cajero cajero) {
        log.info("Creando nuevo cajero: {}", cajero.getCodigo());
        cajero.setFechaCreacion(LocalDateTime.now());
        cajero.setActivo(true);
        return cajeroRepository.save(cajero);
    }
    
    public Cajero update(Cajero cajero) {
        log.info("Actualizando cajero: {}", cajero.getId());
        if (cajeroRepository.existsById(cajero.getId())) {
            cajero.setFechaActualizacion(LocalDateTime.now());
            return cajeroRepository.save(cajero);
        }
        throw new NotFoundException(cajero.getId(), "Cajero");
    }
    
    public void delete(String id) {
        log.info("Eliminando cajero: {}", id);
        if (cajeroRepository.existsById(id)) {
            cajeroRepository.deleteById(id);
        } else {
            throw new NotFoundException(id, "Cajero");
        }
    }

    public Page<Cajero> findAll(Pageable pageable) {
        log.info("Buscando todos los cajeros paginados: {}", pageable);
        return cajeroRepository.findAll(pageable);
    }
    
    public Page<Cajero> findByAgencia(String agencia, Pageable pageable) {
        log.info("Buscando cajeros por agencia '{}' paginados: {}", agencia, pageable);
        return cajeroRepository.findByAgencia(agencia, pageable);
    }
    
    public Page<Cajero> findActivos(Pageable pageable) {
        log.info("Buscando cajeros activos paginados: {}", pageable);
        return cajeroRepository.findByActivoTrue(pageable);
    }
} 