package com.grupop.gestion.DTO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class FormaDePagoDetalleDTO {


        private String descripcion;
        private BigDecimal importe;


}
