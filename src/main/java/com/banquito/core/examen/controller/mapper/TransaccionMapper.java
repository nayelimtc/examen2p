package com.banquito.core.examen.controller.mapper;

import com.banquito.core.examen.controller.dto.TransaccionDTO;
import com.banquito.core.examen.model.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransaccionMapper {

    TransaccionDTO toDTO(Transaccion model);
    
    Transaccion toModel(TransaccionDTO dto);
    
    List<TransaccionDTO> toDTOList(List<Transaccion> models);
    
    List<Transaccion> toModelList(List<TransaccionDTO> dtos);
} 