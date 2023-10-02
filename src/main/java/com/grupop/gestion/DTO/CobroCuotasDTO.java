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
public class CobroCuotasDTO {

    private Long idCobro;
    private Integer talonario;
    private String clienteId;
    private Long creditoId;
    private Integer nroCuota;
    private String planPago;
    private String fechaCobro;
    private BigDecimal importeCuota;
    private BigDecimal importeAjuste;
    private BigDecimal importeIntereses;
    private BigDecimal importeBonificacion;
    private BigDecimal importeACobrar;
    private Boolean cancelo;



}
