package com.banquito.core.examen.controller.mapper;

import com.banquito.core.examen.controller.dto.TurnoDTO;
import com.banquito.core.examen.model.Turno;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TurnoMapper {

    TurnoDTO toDTO(Turno model);
    
    Turno toModel(TurnoDTO dto);
    
    List<TurnoDTO> toDTOList(List<Turno> models);
    
    List<Turno> toModelList(List<TurnoDTO> dtos);
} 