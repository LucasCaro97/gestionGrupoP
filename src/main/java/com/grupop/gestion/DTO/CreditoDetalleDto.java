package com.grupop.gestion.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreditoDetalleDto {

    private Long id;
    private String cliente;
    private Long idCredito;
    private Integer nroCuota;
    private BigDecimal capital;
    private BigDecimal gastoAdm;
    private BigDecimal monto;
    private LocalDate vencimiento;
    private BigDecimal saldo;
    private String diasAtrasados;
    private BigDecimal interesPun;
    private BigDecimal ajusteCac;
    private BigDecimal total;

}
