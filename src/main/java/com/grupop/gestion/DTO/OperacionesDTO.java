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
public class OperacionesDTO {

    private Long id;
    private Long tipoOperacion;
    private String razonSocial;
    private LocalDate fechaOperacion;
    private Integer talonario;
    private String nroComprobante;
    private String sector;
    private String formaPago;
    private BigDecimal total;
    private String observacion;

}
