package com.banquito.core.examen.controller.mapper;

import com.banquito.core.examen.controller.dto.CajeroDTO;
import com.banquito.core.examen.model.Cajero;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CajeroMapper {

    CajeroDTO toDTO(Cajero model);
    
    Cajero toModel(CajeroDTO dto);
    
    List<CajeroDTO> toDTOList(List<Cajero> models);
    
    List<Cajero> toModelList(List<CajeroDTO> dtos);
} 