package com.banquito.core.examen.controller.mapper;

import com.banquito.core.examen.controller.dto.DetalleDenominacionDTO;
import com.banquito.core.examen.model.DetalleDenominacion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DetalleDenominacionMapper {

    DetalleDenominacionDTO toDTO(DetalleDenominacion model);
    
    DetalleDenominacion toModel(DetalleDenominacionDTO dto);
    
    List<DetalleDenominacionDTO> toDTOList(List<DetalleDenominacion> models);
    
    List<DetalleDenominacion> toModelList(List<DetalleDenominacionDTO> dtos);
} 