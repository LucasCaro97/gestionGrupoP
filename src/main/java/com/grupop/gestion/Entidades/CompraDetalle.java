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
public class CompraDetalle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_compra")
    @ManyToOne
    private Compra compraId;
    @JoinColumn(name = "fk_producto")
    @OneToOne
    private Producto producto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioFinal;
    private BigDecimal totalsinImpuesto;
    private BigDecimal impuesto;
    private BigDecimal total;




}
