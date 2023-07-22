package com.grupop.gestion.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreditoDetalleDto {

    private Integer nroCuota;
    private BigDecimal monto;
    private LocalDate vencimiento;

}
