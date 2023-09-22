package com.grupop.gestion.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CobroDetalleCuotasReporteDto {


    private String descripcion;
    private BigDecimal intereses;
    private BigDecimal total;

}