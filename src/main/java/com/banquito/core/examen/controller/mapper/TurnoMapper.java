package com.banquito.management.controller.mapper;

import com.banquito.management.controller.dto.TurnoDTO;
import com.banquito.management.model.Turno;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DetalleDenominacionMapper.class}
)
public interface TurnoMapper {

    TurnoDTO toDTO(Turno model);
    
    Turno toModel(TurnoDTO dto);
    
    List<TurnoDTO> toDTOList(List<Turno> models);
    
    List<Turno> toModelList(List<TurnoDTO> dtos);
} 