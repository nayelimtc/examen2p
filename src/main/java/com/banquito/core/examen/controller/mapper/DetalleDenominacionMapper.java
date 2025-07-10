package com.banquito.management.controller.mapper;

import com.banquito.management.controller.dto.DetalleDenominacionDTO;
import com.banquito.management.model.DetalleDenominacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DetalleDenominacionMapper {

    @Mapping(target = "valorTotal", expression = "java(model.getValorTotal())")
    DetalleDenominacionDTO toDTO(DetalleDenominacion model);
    
    DetalleDenominacion toModel(DetalleDenominacionDTO dto);
    
    List<DetalleDenominacionDTO> toDTOList(List<DetalleDenominacion> models);
    
    List<DetalleDenominacion> toModelList(List<DetalleDenominacionDTO> dtos);
} 