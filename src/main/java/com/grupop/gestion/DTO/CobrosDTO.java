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
public class CobrosDTO {

    private Long id;
    private String cliente;
    private LocalDate fechaComprobante;
    private Integer talonario;
    private String nroComprobante;
    private String sector;
    private BigDecimal total;
    private String formaPago;


}

