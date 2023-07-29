package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class FormaDePagoDetalleSubDetalle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "idOperacion", referencedColumnName = "idOperacion"),
            @JoinColumn(name = "tipoOperacion", referencedColumnName = "tipoOperacion")
    })
    private FormaDePagoDetalle formaDePagoDetalle;

    @JoinColumn(name = "fk_formaDePago")
    @ManyToOne
    private FormaDePago formaPago;
    private BigDecimal monto;

}
