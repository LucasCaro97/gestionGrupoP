package com.grupop.gestion.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ComprasDTO {

    private Long id;
    private String entidad;
    private Long tipoOperacion;
    private LocalDate fechaComprobante;
    private Integer talonario;
    private String nroComprobante;
    private String sector;
    private BigDecimal total;
    private String formaPago;


}
