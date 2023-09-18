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
public class CobroDetalleCuotasDto {


    private Long idCobro;
    private Long idVenta;
    private Long idCredito;
    private Integer nroCuota;
    private String fechaVenc;
    private BigDecimal cuotaBase;
    private BigDecimal ajuste;
    private BigDecimal punitorio;
    private BigDecimal importeBonif;
    private BigDecimal importeFinal;
    private BigDecimal importeCobrado;

}