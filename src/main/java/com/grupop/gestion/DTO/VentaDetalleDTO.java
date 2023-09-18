package com.grupop.gestion.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VentaDetalleDTO {


    private Long idVenta;
    private Long idProd;
    private Double cantidad;
    private Double precioU;

}
