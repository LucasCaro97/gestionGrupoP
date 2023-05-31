package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VentaDetalle implements Serializable {

    @EmbeddedId
    private VentaDetallePK ventaDetallePK;

    @MapsId("id")
    @ManyToOne
    private Venta ventaId;
    @JoinColumn(name = "fk_producto")
    @OneToOne
    private Producto producto;
    private Double cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioFinal;
    private BigDecimal precioConImpuesto;
    private BigDecimal precioSinImpuesto;
    private BigDecimal impuestos;
    private BigDecimal total;


}
