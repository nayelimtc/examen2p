package com.banquito.core.examen.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "cajeros")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cajero {
    
    @Id
    @EqualsAndHashCode.Include
    private String id;
    
    private String codigo;
    private String codigoCaja;
    private String nombre;
    private String apellido;
    private String email;
    private String agencia;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public Cajero(String id) {
        this.id = id;
    }
} 