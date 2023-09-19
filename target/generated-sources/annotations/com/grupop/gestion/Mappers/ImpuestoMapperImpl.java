package com.grupop.gestion.Mappers;

import com.grupop.gestion.DTO.ImpuestoDto;
import com.grupop.gestion.Entidades.Impuestos;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-19T18:53:20-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
public class ImpuestoMapperImpl implements ImpuestoMapper {

    @Override
    public Impuestos toImpuestos(ImpuestoDto impuestoDto) {
        if ( impuestoDto == null ) {
            return null;
        }

        Impuestos impuestos = new Impuestos();

        impuestos.setId( impuestoDto.getId() );
        if ( impuestoDto.getDescripcion() != null ) {
            impuestos.setDescripcion( String.valueOf( impuestoDto.getDescripcion() ) );
        }

        return impuestos;
    }
}
