package com.grupop.gestion.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CompraDetalleImputacionDto {

    private Long compraId;
    private Long cuentaContableId;
    private BigDecimal importeBase;
    private List<Long> impuestosIds;
    private BigDecimal importeTotal;


}
