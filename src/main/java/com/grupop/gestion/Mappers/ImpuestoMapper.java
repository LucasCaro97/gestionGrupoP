package com.grupop.gestion.Mappers;

import com.grupop.gestion.DTO.ImpuestoDto;
import com.grupop.gestion.Entidades.Impuestos;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImpuestoMapper {
    ImpuestoMapper INSTANCE = Mappers.getMapper(ImpuestoMapper.class);

    Impuestos toImpuestos(ImpuestoDto impuestoDto);

}
