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
public class CobroDetalleCtaCteDTO {

    private Long idCobro;
    private Long idVenta;
    private BigDecimal importe;

}
