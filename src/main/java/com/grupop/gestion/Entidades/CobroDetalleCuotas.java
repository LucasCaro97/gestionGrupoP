package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CobroDetalleCuotas {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cobro cobroId;
    @JoinColumn(name="fk_venta")
    @ManyToOne
    private Venta ventaId;
    @JoinColumn(name = "fk_credito")
    @ManyToOne
    private Credito creditoId;
    private Integer nroCuota;
    private LocalDate fechaVencimiento;
    private BigDecimal importeCuota;
    private BigDecimal importeACobrar;
    private BigDecimal importeIntereses;
    private BigDecimal importeAjuste;
    private BigDecimal porcIndice;
    private BigDecimal importeBonificacion;
    private Double porcBonificacion;
    private BigDecimal importeFinal;
    private Boolean cancelo;    


}
