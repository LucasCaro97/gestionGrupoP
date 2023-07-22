package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CobroDetalleCtaCte {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cobro cobroId;
    @JoinColumn(name="fk_venta")
    @ManyToOne
    private Venta ventaId;
    private String detalle;
    private BigDecimal totalDetalle;


}
